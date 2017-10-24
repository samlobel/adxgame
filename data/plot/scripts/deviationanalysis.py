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
from itertools import chain


def get_majority_vote(demand_factor, impressions, number_of_agents):
    list_of_frames = []
    
    #number_of_games = 100
    grid_number_games = [100,200,400]
    
    for number_of_games in grid_number_games:
        print('Majority Vote for #games = ', number_of_games)
        dir_location = setup.get_agent_dir_location(number_of_games, impressions, demand_factor)
        profile_data = deviation_graphs.produce_profile_data(number_of_games, dir_location, number_of_agents = number_of_agents)
        DG = deviation_graphs.produce_deviation_graph(profile_data)
        
        P = nx.adjacency_matrix(DG)
        
        deviation_data = {}
        i = 0
        number_profiles = len(DG.nodes())
        for node in DG.nodes():
            deviation_forward = 0
            deviation_backward = 0
            if i < number_profiles - 1:
                deviation_forward = P[i, i + 1]
            if i > 0:
                deviation_backward = P[i, i - 1]
            deviation_data[node] = (deviation_backward, deviation_forward)
            i = i + 1
        list_of_frames.append(pd.DataFrame.from_dict(deviation_data, orient='index'))
    
    x = pd.concat(list_of_frames, axis=1)
    x.columns = list(chain.from_iterable(('back_'+str(n),'forw_'+str(n)) for n in grid_number_games))
    x.loc[((x.back_100 == x.back_200) & (x.forw_100 == x.forw_200)), 'vote_1']= 1
    x.loc[((x.back_200 == x.back_400) & (x.forw_200 == x.forw_400)), 'vote_2']= 1
    x.loc[((x.back_100 == x.back_400) & (x.forw_100 == x.forw_400)), 'vote_3']= 1
    x = x.fillna(0)
    x['final_vote'] = (x.vote_1 + x.vote_2 + x.vote_3) / 3.0
    temp = x.to_dict()
    return temp['final_vote']