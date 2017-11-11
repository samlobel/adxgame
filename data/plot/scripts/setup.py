#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Thu Oct 19 11:59:12 2017

@author: enriqueareyan
"""
import itertools
import os
demand_list = ['0.25', '0.5', '0.75', 
               '1.0', '1.25', '1.5', '1.75', 
               '2.0', '2.25', '2.5', '2.75', 
               '3.0']
#demand_list = ['1.75', '2.25', '2.75', '3.0']
#demand_list = ['2.75']
supply_list = [2000]

path_to_data = '/home/eareyanv/workspace/adxgame/data/'
path_to_results = '/home/eareyanv/workspace/adxgame/data/results/'

def get_grid_supply_demand():
    return itertools.product(supply_list, demand_list)

def get_two_agents_combinations():
    return itertools.combinations(['SI','WE','WF'], 2)

def get_zip_location(zip_suffix, number_of_games):
    return path_to_results + str(number_of_games) + zip_suffix + '.zip'

def get_agent_dir_location(number_of_games, supply, demand):
    return str(number_of_games) + '/' + str(supply) + '/' + demand + '/' + 'agents/'

def get_file_location(numberWE, numberWF, reserve  = None):
    return 'WEWF(' + str(numberWE) + '-' + str(numberWF) + ')' + ('-r(' + str(reserve) + ')' if reserve is not None else '') + '.csv'

def get_market_maker_dir_location(number_of_games, supply, demand):
    return str(number_of_games) + '/' + supply + '/' + demand + '/' + 'marketmaker/'

def create_dir(directory):
    if not os.path.exists(directory):
        os.makedirs(directory)
    return directory