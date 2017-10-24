#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Thu Oct 19 11:59:12 2017

@author: enriqueareyan
"""
from itertools import product, combinations

demand_factors = ['0.25', '0.5', '0.75', '1.0', '1.25', '1.5', '1.75', '2.0', '2.25', '2.5', '2.75', '3.0']
#demand_factors = ['3.0']
impressions = ['2000']        

def get_grid_demand_impressions():
    return product(demand_factors, impressions)

def get_two_agents_combinations():
    return combinations(['SI','WE','WF'], 2)

def get_agent_dir_location(number_of_games, supply, demand):
    return str(number_of_games) + '/' + supply + '/' + demand + '/' + 'agents/'
def get_market_maker_dir_location(number_of_games, supply, demand):
    return str(number_of_games) + '/' + supply + '/' + demand + '/' + 'marketmaker/'    