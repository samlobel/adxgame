#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Thu Oct 26 20:06:36 2017

@author: eareyanv
"""
import zipfile
import deviation_graphs
import deviationanalysis
import setup
#def check_if_zip_contains_file(zip_location, filename):
    
    
#def check_if_profile_is_in_zip()

def get_cascade_profile(number_of_games, number_of_agents, number_WE, number_WF, impressions, demand_factor):
    """
    Given an initial number of games, number_of_games, 
    cascades back to the first time the profile in 
    file_location is found. This function iteratively
    halfs number_of_games until a profile is found
    OR we get below 200, in which case an error is thrown.
    """
    while(number_of_games >= 100):
        zf = zipfile.ZipFile('../../results/' + str(number_of_games) + '.zip')
        file_location = setup.get_agent_dir_location(number_of_games, impressions, demand_factor) + 'WEWF(' + str(number_WE) + '-' + str(number_WF) + ').csv'
        print('Searching for ', file_location)
        if file_location in zf.namelist():
            print('Found!')
            return number_of_games
        else:
            print('Not found for number_of_games = ', number_of_games, ', trying half')
        number_of_games = int(number_of_games / 2)
    raise ValueError('Could not find a sample for ', file_location)
        

def compute_cascade_graph(number_of_games, number_of_agents, impressions, demand_factor):
    """
    This function will build a cascading graph, where we first look for 
    sample data with exactly number_of_games samples, and if we cannot find
    it, we try number_of_samples/2, and so on.
    If call with number_of_games = 100 OR number_of_games = 200, it 
    will find all samples by definition and thus return the graph where
    all the samples are 100 OR 200 respectively.
    """
    print('Computing cascade graph for number_of_games =', number_of_games, ', number_of_agents = ', number_of_agents, ', impressions = ', impressions, ', demand_factor = ', demand_factor)
    cascade_map_to_number_of_games = dict(('WE' * (number_of_agents-i) + 'WF' * i, get_cascade_profile(number_of_games, number_of_agents,number_of_agents - i, i, impressions, demand_factor)) for i in range(0,number_of_agents + 1))
    return deviation_graphs.produce_specific_profile_data(cascade_map_to_number_of_games, number_of_agents, impressions, demand_factor)

DG_0 = deviation_graphs.produce_deviation_graph(compute_cascade_graph(100, 5, '2000','0.5'))
DG_1 = deviation_graphs.produce_deviation_graph(compute_cascade_graph(200, 5, '2000','0.5'))
DG_2 = deviation_graphs.produce_deviation_graph(compute_cascade_graph(400, 5, '2000','0.5'))
deviation_100_200 = deviationanalysis.get_agreement_data(DG_0, DG_1)
deviation_200_400 = deviationanalysis.get_agreement_data(DG_1, DG_2)
