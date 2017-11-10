#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Mon Oct 23 10:58:01 2017

@author: enriqueareyan
"""
import mean_best_response_graphs
import deviation_analysis
import zipfile
import setup
import networkx as nx
import pandas as pd
from itertools import chain

def get_agreement_data(n1, n2, G1, G2):
    """
    Given a pair of deviation graphs G1, G2, returns a list
    of frames containing, for each profile, whether there is
    agreement between the graphs.
    """
    list_of_frames = []
    graphs = [G1, G2]
    for graph in graphs:
        i = 0
        number_profiles = len(graph.nodes())
        # Get the adjacency matrix: it is easier to compute with it.
        P = nx.adjacency_matrix(graph)
        deviation_data = {}
        for node in graph.nodes():
            # An agent might wish to deviation forward (switch to a WF) or backward (switch to a WE)
            deviation_forward = 0
            deviation_backward = 0
            # Border cases: if all WF, then there is no going forward. Likewise, if all WE then there is no going backward.
            if i < number_profiles - 1:
                deviation_forward = P[i, i + 1]
            if i > 0:
                deviation_backward = P[i, i - 1]
            deviation_data[node] = (deviation_backward, deviation_forward)
            i = i + 1
        dataframe = pd.DataFrame.from_dict(deviation_data, orient='index')
        dataframe.columns = ['deviation_backward', 'deviation_forward']
        list_of_frames.append(dataframe)
    dataframe = pd.concat(list_of_frames, axis = 1)
    dataframe.columns = list(chain.from_iterable(('back_' + str(n),'forw_' + str(n)) for n in [n1, n2]))
    return dataframe

def get_edge_agreement(n1, n2, supply, demand, deviation_dataframe):
    """
    Given n1, n2, supply, demand and a data frame containing deviation data,
    produce a dataframe with row (Strategy1, Strategy2, n1 deviation, n2 deviation),
    where strategy 1 is a neighbor of sttrategy 2, and n1/n2 deviation is either
    forward in case Strategy 1 -> Strategy 2 or backward in case Strategy2 -> Strategy 1.
    """
    number_of_strategies = len(deviation_dataframe)
    list_of_edges = []
    for i in range(0, number_of_strategies - 1):
        WE_strategy_1 = deviation_dataframe.iloc[i].name.count('WE')
        WF_strategy_1 = deviation_dataframe.iloc[i].name.count('WF')
        WE_strategy_2 = deviation_dataframe.iloc[i+1].name.count('WE')
        WF_strategy_2 = deviation_dataframe.iloc[i+1].name.count('WF')
        strategy_tuple = (WE_strategy_1, WF_strategy_1, WE_strategy_2, WF_strategy_2)
        for n in [n1, n2]:
            if((deviation_dataframe.iloc[i + 1]['back_' + str(n)] == 0) and (deviation_dataframe.iloc[i]['forw_' + str(n)] == 1)):
                strategy_tuple  = strategy_tuple + ('f',)
            elif((deviation_dataframe.iloc[i + 1]['back_' + str(n)] == 1) and (deviation_dataframe.iloc[i]['forw_' + str(n)] == 0)):
                strategy_tuple  = strategy_tuple + ('b',)
            elif((deviation_dataframe.iloc[i + 1]['back_' + str(n)] == 1) and (deviation_dataframe.iloc[i]['forw_' + str(n)] == 1)):
                strategy_tuple  = strategy_tuple + ('d',)
            else:
                raise ValueError('Deviation not understood!!!')
        list_of_edges = list_of_edges + [strategy_tuple]
    dataframe = pd.DataFrame(list_of_edges)
    dataframe['impressions'] = supply
    dataframe['demand_factor'] = demand
    dataframe.columns = ['WE1', 'WF1', 'WE2', 'WF2', 'direction_' + str(n1), 'direction_' + str(n2), 'impressions', 'demand_factor']
    dataframe = dataframe[['WE1', 'WF1', 'WE2', 'WF2', 'impressions', 'demand_factor', 'direction_' + str(n1), 'direction_' + str(n2)]]
    return dataframe

def get_direction_dataframe(zip_suffix, n1, n2, number_of_agents, supply, demand, reserve = None):
    """
    Given number of samples n1, n2; number_of_agents, supply and demand,
    computes and the agreements between the cascades graphs generated by n1, n2
    and returns a dataframe with them.
    """
    DG_0_profile_data = compute_cascade_profile_data(zip_suffix, n1, number_of_agents, supply, demand, reserve)
    DG_1_profile_data = compute_cascade_profile_data(zip_suffix, n2, number_of_agents, supply, demand, reserve)
    DG_0 = mean_best_response_graphs.produce_mean_best_response_graph(DG_0_profile_data)
    DG_1 = mean_best_response_graphs.produce_mean_best_response_graph(DG_1_profile_data)
    deviation_dataframe = deviation_analysis.get_agreement_data(n1, n2, DG_0, DG_1)
    deviation_dataframe = get_edge_agreement(n1, n2, supply, demand, deviation_dataframe)
    return deviation_dataframe

def determine_cascade_profile(zip_suffix, number_of_games, numberWE, numberWF, supply, demand, reserve = None):
    """
    Given an initial number of games, number_of_games, 
    cascades back to the first time the profile in 
    file_location is found. This function iteratively
    halfs number_of_games until a profile is found
    OR we get below 200, in which case an error is thrown.
    """
    while(number_of_games >= 100):
        zip_location = setup.get_zip_location(zip_suffix, number_of_games)
        zf = zipfile.ZipFile(zip_location)
        file_location = setup.get_agent_dir_location(number_of_games, supply, demand) + setup.get_file_location(numberWE, numberWF, reserve) 
        #print('Searching for ', file_location, ', in zip ', zip_location)
        if file_location in zf.namelist():
            print('\tFound! =', number_of_games, ', (WE, WF) = (', numberWE,',',numberWF, ')', (' reserve = '+str(reserve) if reserve is not None else ''))
            print('\t', zip_location)
            return number_of_games
        else:
            print('\tNot found for number_of_games = ', number_of_games, ', trying half')
        number_of_games = int(number_of_games / 2)
    raise ValueError("******************* Could not find data for file_location ", file_location, ", in zip ", zip_location)

def compute_cascade_profile_data(zip_suffix, number_of_games, number_of_agents, supply, demand, reserve = None):
    """
    This function will build a cascading graph, where we first look for 
    sample data with exactly number_of_games samples, and if we cannot find
    it, we try number_of_samples/2, and so on.
    If call with number_of_games = 100 OR number_of_games = 200, it 
    will find all samples by definition and thus return the graph where
    all the samples are 100 OR 200 respectively.
    """
    print('Computing cascade graph for:' ,
          '\n\t number_of_games =', number_of_games, 
          '\n\t number_of_agents = ', number_of_agents, 
          '\n\t impressions = ', supply, 
          '\n\t demand_factor = ', demand)
    cascade_map_to_number_of_games = dict(('WE' * (number_of_agents-i) + 'WF' * i, determine_cascade_profile(zip_suffix, number_of_games, number_of_agents - i, i, supply, demand, reserve)) for i in range(0,number_of_agents + 1))
    return mean_best_response_graphs.produce_specific_profile_data(zip_suffix, cascade_map_to_number_of_games, number_of_agents, supply, demand, reserve)

def get_best_response_graph(zip_suffix, number_of_games, number_of_agents, supply, demand, reserve = None):
    """
    Gets the best_response graph for given parameters using the cascading profile.
    """
    profile_data = deviation_analysis.compute_cascade_profile_data(zip_suffix, number_of_games, number_of_agents, supply, demand, reserve)
    return mean_best_response_graphs.produce_mean_best_response_graph(profile_data)

def save_agreement_data(n1, n2, number_of_agents):
    """
    Given number of samples n1, n2; and number_of_agents, saves the agreement data
    for all supply and demand factors to a .csv file
    """
    list_of_dataframes = []
    print('Computing agreement data for ', number_of_agents, ' agents, between ', n1, ' and ', n2 ,' samples')
    for supply, demand in setup.get_grid_supply_demand():
        print('\t(supply, demand) = (', supply, ',', demand, ')')
        list_of_dataframes += [get_direction_dataframe(n1, n2, number_of_agents, supply, demand)]
    final_dataframe = pd.concat(list_of_dataframes)
    final_dataframe.to_csv(setup.path_to_data + 'stability/' + str(n1) + '-' + str(n2) + '/stability-for-' + str(number_of_agents) + '-agents.csv', index = False)