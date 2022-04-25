#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Sat Nov  4 20:10:32 2017

@author: enriqueareyan
"""

import setup
import deviation_analysis
import get_data
import networkx as nx
import pandas as pd
import numpy as np
from matplotlib import pyplot as plt

def has_outgoing_edges(DG, component):
    """
    Given an strongly connected component,
    return true if the component has outgoing edges
    to other nodes outside the component, false otherwise.
    """
    for node in component:
        for out_edge in DG.out_edges(node):
            if(not out_edge[1] in component):
                return True
    return False

def save_sink_eq_stats(zip_suffix, number_of_games, number_of_agents, reserve = None):
    """
    Given the number of games and the number of agents,
    save the sink equilibria stats.
    """
    list_of_dataframes = []
    i = 0
    for supply, demand in setup.get_grid_supply_demand():
        print('(supply, demand) = (' , supply , ',' , demand , ')') 
        DG = deviation_analysis.get_best_response_graph(zip_suffix, number_of_games, number_of_agents, supply, demand, reserve)
        list_of_sink_eq = []
        for component in nx.strongly_connected_components(DG):
            if(not has_outgoing_edges(DG, component)):
                list_of_sink_eq += [(number_of_agents, supply, demand, node.count('WE'), node.count('WF'), i) + get_profile_revenue(zip_suffix, number_of_games, node.count('WE'), node.count('WF'), supply, demand, reserve) for node in component]
                i = i + 1
        dataframe = pd.DataFrame(list_of_sink_eq)
        list_of_dataframes += [dataframe]
    final_dataframe = pd.concat(list_of_dataframes)
    final_dataframe.columns = ['number_of_agents', 'impressions', 'demand_factor', 'WE', 'WF', 'sink_index', 'revenue_mean', 'revenue_lb', 'revenue_ub']
    final_dataframe.to_csv(setup.create_dir(setup.path_to_data + 'sinkequilibria' + ('-reserve' if reserve is not None else '') + '/' + str(number_of_games) + '/') + 'sink-equilibria-for-' + str(number_of_agents) + '-agents' + ('-reserve-' + str(reserve) if reserve is not None else '') + '.csv', index = False)


def get_profile_revenue(zip_suffix, number_of_games, numberWE, numberWF, supply, demand, reserve = None):
    """
    Given the description of a profile, returns the mean revenue of the market maker.
    """
    number_of_games = deviation_analysis.determine_cascade_profile(zip_suffix, number_of_games, numberWE, numberWF, supply, demand, reserve)
    zip_location = setup.get_zip_location(zip_suffix,number_of_games)
    file_location = setup.get_agent_dir_location(number_of_games, supply, demand) + setup.get_file_location(numberWE, numberWF, reserve) 
    data = get_data.get_agent_data(zip_location, file_location)
    data = data.dropna()
    return get_data.mean_confidence_interval(data.wincost)

def plot_sink_stats():
    # Compute average number of sinks and average size of sink.
    avg_number_sinks = []
    avg_size_sink = []
    avg_number_WE_per_player = []
    avg_number_WF_per_player = []
    for number_of_agents in range(2,21):
        data = pd.read_csv('../../sinkequilibria/800/sink-equilibria-for-' + str(number_of_agents) + '-agents.csv')
        avg_number_sinks +=  [(data.sink_index.nunique() / 12)]
        avg_size_sink += [data.groupby('sink_index').size().mean()]
        avg_number_WE_per_player += [data.groupby('sink_index').mean().mean()['WE'] / number_of_agents]
        avg_number_WF_per_player += [data.groupby('sink_index').mean().mean()['WF'] / number_of_agents]
        
    fig, ax = plt.subplots(nrows = 2, ncols = 1, sharex = True)
    x = [i for i in range (2,21)]
    ax[0].plot(x, avg_number_sinks, label = 'Average number of sinks',linestyle = ':')#, color = 'red')
    ax[0].plot(x, avg_size_sink, label = 'Average size of sinks',linestyle = ':')#, color = 'green')
    ax[1].plot(x, avg_number_WF_per_player, label = 'Avg. #WF/#player')
    ax[1].plot(x, avg_number_WE_per_player, label = 'Avg. #WE/#player')
    plt.xlabel('Number of agents')
    plt.xticks(np.arange(min(x), max(x)+1, 1.0))
    ax[0].legend()
    ax[1].legend()
    plt.show()

