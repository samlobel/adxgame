#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Mon Oct 23 10:58:01 2017

@author: enriqueareyan
"""
import networkx as nx
import pandas as pd
import deviation_graphs
from itertools import chain, product


def get_agreement_data(G1, G2):
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
        list_of_frames.append(pd.DataFrame.from_dict(deviation_data, orient='index'))
    return list_of_frames

def agreement_data_to_dict(n1, n2, list_of_frames):
    # Convert all the information to a dataframe for ease of manipulation.
    x = pd.concat(list_of_frames, axis = 1)
    x.columns = list(chain.from_iterable(('back_' + str(n),'forw_' + str(n)) for n in [n1, n2]))
    x.loc[((x['back_' + str(n1)] == x['back_' + str(n2)]) & (x['forw_' + str(n1)] == x['forw_' + str(n2)])), str(n1)+ '-' + str(n2) + '-agreement'] = 1
    # By filling na with 0, we account for the case where the samples disagree.
    x = x.fillna(0)
    return x.to_dict()

def compute_agreement(n1, n2, impressions, demand_factor, number_of_agents):
    """
    Given number of games n1, n2 (for example n1 = 100 and n2 = 200), impressions
    demand_factor, and number_of_agents, returns a dictionary {Strategy s : 1 or 0},
    where 1 indicates that the samples for n1 and n2 agrees for strategy s.
    This function assumes that all samples are present for sample numbers n1, n2.
    This function is used mainly for n1 = 100, n2 = 200.
    """
    grid_number_games = [n1, n2]
    graphs = []
    for number_of_games in grid_number_games:
        print('Agreement computation for #games = ', number_of_games)
        # Create the deviation graph for the fix number of games.
        profile_data = deviation_graphs.produce_profile_data(number_of_games, number_of_agents, impressions, demand_factor)
        DG = deviation_graphs.produce_deviation_graph(profile_data)
        graphs += [DG]
    temp = agreement_data_to_dict(get_agreement_data(graphs[0], graphs[1]))
    return temp[str(n1)+ '-' + str(n2) +'-agreement']

def compute_all_initial_agreements(n1, n2, impressions, demand_factors, agents):
    """
    Compute all the agreement between number of games n1 and n2, for the
    given impressions, demand_factors and agents. Saves the results to .csv.
    """
    print('Computing all agreements for every profile:')
    for number_agents in agents:
        votes = []
        print('\t Agents: ', number_agents)
        for demand, supply in product(demand_factors, impressions):
            print('\t\t (demand, supply) = (', demand, ',', supply, ')')
            temp = compute_agreement(n1, n2, supply, demand, number_agents)
            votes = votes + [(profile.count('WE'), profile.count('WF'), supply, demand, votes_for_profile) for profile, votes_for_profile in temp.items()]
        dataframe = pd.DataFrame(votes)
        dataframe.columns = ['WE','WF','impressions','demand_factor', str(n1) + '-' + str(n2) + '-agreement']
        dataframe.to_csv('../../stability/' + str(n1) + '-' + str(n2) + '/stability-for-' + str(number_agents) + '-agents.csv', index = False)

#compute_all_agreements(100,200,setup.impressions, setup.demand_factors, range(2,21))

def refine_conflicts(previous_n1, previous_n2, next_n1, supply, demand_factor, number_of_agents):
    data = pd.read_csv('../../stability/' + str(previous_n1) + '-' + str(previous_n2) + '/stability-for-' + str(number_of_agents) + '-agents.csv')
    data = data[(data['impressions'] == int(supply)) & (data['demand_factor'] == demand_factor)]

    # Check if there is any disagreement, otherwise don't bother
    if(len(data[data[str(previous_n1) + '-' + str(previous_n2) + '-agreement'] == 0]) > 0):
        # First, construct the map of profiles where if agreeement exists, we take previous_n2 else we take next_n1
        map_profile_to_numberofgames = {}
        for index, row in data.iterrows():
            map_profile_to_numberofgames['WE' * int(row['WE']) + 'WF' * int(row['WF'])] = (previous_n2 if row[str(previous_n1) + '-' + str(previous_n2) + '-agreement'] == 1 else next_n1)
        profile_data_1 = deviation_graphs.produce_specific_profile_data(map_profile_to_numberofgames, number_of_agents, supply, str(demand_factor))
        DG_1 = deviation_graphs.produce_deviation_graph(profile_data_1)
        
        # Second, construct the graph with the previous_n2 samples
        profile_data_2 = deviation_graphs.produce_profile_data(previous_n2, number_of_agents, supply, str(demand_factor))
        DG_2 = deviation_graphs.produce_deviation_graph(profile_data_2)
        
        # Compare the two graphs, one with 
        agree = get_agreement_data(DG_1, DG_2)
        agree_dict = agreement_data_to_dict(previous_n1, previous_n2, agree)
        new_agreement = agree_dict[str(previous_n1)+ '-' + str(previous_n2) +'-agreement']
        
        final_data = []
        for index,row in data.iterrows():
            if row[str(previous_n1) + '-' + str(previous_n2) + '-agreement'] == 0:
                final_data = final_data + [(row['WE'], row['WF'], row['impressions'], row['demand_factor'], new_agreement['WE' * int(row['WE']) + 'WF' * int(row['WF'])])]
        final_dataframe = pd.DataFrame(final_data)
        final_dataframe.columns = ['WE','WF','impressions','demand_factor', str(previous_n2) + '-' + str(next_n1) + '-agreement']
        #final_data.to_csv('../../stability/' + str(n1) + '-' + str(n2) + '/stability-for-' + str(number_agents) + '-agents.csv', index = False)
        return final_dataframe
    
for i in range(2,3):
    dataframes_agreements = []
    print('\t Agents: ', i)
    for demand, supply in product([0.25,0.5], ['2000']):
        print('\t\t (demand, supply) = (', demand, ',', supply, ')')
        temp = refine_conflicts(100,200,400,'2000',demand,i)
        dataframes_agreements = dataframes_agreements + [temp]
    dataframe = pd.concat(dataframes_agreements)
    dataframe.columns = ['WE','WF','impressions','demand_factor', str(200) + '-' + str(400) + '-agreement']
