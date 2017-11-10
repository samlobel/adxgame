#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Wed Nov  8 15:04:09 2017

@author: eareyanv
"""
import deviation_analysis
import pandas as pd
import setup
import sys
import stats_mean_best_response_graphs

def save_reserve_agreement_data(n1, n2, reserve):
    list_of_dataframes = []
    number_of_agents = 8
    print('Computing agreement data for ', number_of_agents, ' agents, reserve ',reserve ,', between ', n1, ' and ', n2 ,' samples')
    for supply, demand in setup.get_grid_supply_demand():
        print('\t(supply, demand) = (', supply, ',', demand, ')')
        list_of_dataframes += [deviation_analysis.get_direction_dataframe(n1, n2, number_of_agents, supply, demand, str(n1) + '-8-agents-vary-reserve', str(n2) + '-8-agents-vary-reserve', reserve)]
    final_dataframe = pd.concat(list_of_dataframes)
    final_dataframe.to_csv(setup.create_dir(setup.path_to_data + 'stability-reserve/' + str(n1) + '-' + str(n2) + '/') + 'stability-for-' + str(number_of_agents) + '-agents-reserve-' + str(reserve) + '.csv', index = False)
    
command_line_arguments = sys.argv
if len(command_line_arguments) > 1:
    reserve_price = command_line_arguments[1]
    print('Call from command line -> reserve_price = ' + str(reserve_price))
    #save_reserve_agreement_data(400, 800, int(reserve_price))
    stats_mean_best_response_graphs.save_sink_eq_stats('-8-agents-vary-reserve', 800, 8, reserve_price)
    
def get_worst_sink_equilibria_profile(number_of_games, number_of_agents, supply, demand, reserve):
    """
    Given supply, demand, and reserve, outputs the profile with lowest
    revenue among all profiles of all sink equilibria.
    """
    data = pd.read_csv(setup.path_to_data + 'sinkequilibria-reserve/' + str(number_of_games) + '/sink-equilibria-for-' + str(number_of_agents) + '-agents-reserve-' + str(reserve) + '.csv')
    data = data[(data['impressions'] == supply) & (data['demand_factor'] == demand)]    
    worst_sink_equilibria_value = float("inf")
    worst_sink_equilibria = None
    grouped = data.groupby('sink_index')
    for name, group in grouped:
        worst_profile = group.loc[group['revenue_mean'].idxmin()]
        if(worst_profile['revenue_mean'] < worst_sink_equilibria_value):
            worst_sink_equilibria = worst_profile
            worst_sink_equilibria_value = worst_profile['revenue_mean']
    if(worst_sink_equilibria is not None):
        return worst_sink_equilibria
    else:
        raise ValueError('Could not find a sink equilibria!!!')
        
# Getting the profiles for the worst case equilibria. 
# get_worst_sink_equilibria_profile(800, 8, 2000, 0.25, 0)
for i in range(0,113):
    x = get_worst_sink_equilibria_profile(800, 8, 2000, 1.0, i)
    print(i, x['WE'], x['WF'])
