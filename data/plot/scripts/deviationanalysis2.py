#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Thu Oct 26 20:06:36 2017

@author: eareyanv
"""
import zipfile
#def check_if_zip_contains_file(zip_location, filename):
    
    
#def check_if_profile_is_in_zip()

def get_cascade_profile(number_of_games, file_location):
    """
    Given an initial number of games, number_of_games, 
    cascades back to the first time the profile in 
    file_location is found. This function iteratively
    halfs number_of_games until a profile is found
    OR we get below 200, in which case an error is thrown.
    """
    while(number_of_games >= 200):
        zf = zipfile.ZipFile('../../results/' + str(number_of_games) + '.zip')
        temp_file_location = str(number_of_games) + '/' + file_location
        print('Searching for ', temp_file_location)
        if temp_file_location in zf.namelist():
            print('Found!')
            return
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
    profile_data_found = False
    i = 0
    while(not(profile_data_found)):
        print('Searching')
        file_location = impressions + '/' + demand_factor + '/agents/WEWF(' + str(number_of_agents - i) + '-' + str(i) + ').csv'
        print(file_location)
    