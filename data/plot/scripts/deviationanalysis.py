#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Mon Oct 23 10:58:01 2017

@author: enriqueareyan
"""
import setup
import networkx as nx
import pandas as pd
import deviation_graphs
from itertools import chain, product

def compute_agreement(n1, n2, impressions, demand_factor, number_of_agents):
    """
    Given number of games n1, n2 (for example n1 = 100 and n2 = 200), impressions
    demand_factor, and number_of_agents, returns a dictionary {Strategy s : 1 or 0},
    where 1 indicates that the samples for n1 and n2 agrees for strategy s
    """
    list_of_frames = []
    grid_number_games = [n1, n2]
    for number_of_games in grid_number_games:
        print('Agreement computation for #games = ', number_of_games)
        # Create the deviation graph for the fix number of games.
        dir_location = setup.get_agent_dir_location(number_of_games, impressions, demand_factor)
        profile_data = deviation_graphs.produce_profile_data(number_of_games, dir_location, number_of_agents = number_of_agents)
        DG = deviation_graphs.produce_deviation_graph(profile_data)
        # Get the adjacency matrix: it is easier to compute with it.
        P = nx.adjacency_matrix(DG)
        deviation_data = {}
        i = 0
        number_profiles = len(DG.nodes())
        for node in DG.nodes():
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
    # Convert all the information to a dataframe for ease of manipulation.
    x = pd.concat(list_of_frames, axis = 1)
    x.columns = list(chain.from_iterable(('back_' + str(n),'forw_' + str(n)) for n in grid_number_games))
    x.loc[((x['back_' + str(n1)] == x['back_' + str(n2)]) & (x['forw_' + str(n1)] == x['forw_' + str(n2)])), str(n1)+ '-' + str(n2) + '-agreement'] = 1
    # By filling na with 0, we account for the case where the samples disagree.
    x = x.fillna(0)
    temp = x.to_dict()
    return temp[str(n1)+ '-' + str(n2) +'-agreement']

def compute_all_agreements(n1, n2, impressions, demand_factors, agents):
    """
    Compute all the agreement between number of games n1 and n2, for the
    given impressions, demand_factors and agents. Saves the results to .csv.
    """
    print('Computing all votes for every profile:')
    for number_agents in agents:
        votes = []
        print('\t Agents: ', number_agents)
        for demand, supply in product(demand_factors, impressions):
            print('\t\t (demand, supply) = (', demand, ',', supply, ')')
            temp = compute_agreement(n1, n2, supply, demand, number_agents)
            votes = votes + [(profile.count('WE'), profile.count('WF'), supply, demand, votes_for_profile) for profile, votes_for_profile in temp.items()]
        dataframe = pd.DataFrame(votes)
        dataframe.columns = ['WE','WF','impressions','demand_factor', str(n1) + '-' + str(n2) + '-agreement']
        dataframe.to_csv('../../results/stability/' + str(n1) + '-' + str(n2) + '/stability-for-' + str(number_agents) + '-agents.csv', index = False)

compute_all_agreements(100,200,setup.impressions, setup.demand_factors, range(2,21))