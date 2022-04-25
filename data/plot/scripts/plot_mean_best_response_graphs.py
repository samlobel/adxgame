#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Sat Nov  4 15:45:43 2017

@author: enriqueareyan
"""
import setup
import mean_best_response_graphs
import deviation_analysis
import networkx as nx
import matplotlib.pyplot as plt

def plot_mean_best_response_graph(number_of_games, number_of_agents, supply, demand):
    """
    Plots the best response graph. This function produces
    a circular type of plot of the best response graph.
    """
    # If the next line is uncommented, then the graph is produced
    # for data of EXACTLY number_of_games. This works only for samples 100, 200
    #profile_data = best_response_graphs.produce_profile_data(number_of_games, number_of_agents, supply, demand)
    # This next line produces the cascade profile for the graph
    # STARTING at number_of_games. Currently, we have data up to number_of_games = 800.
    DG = deviation_analysis.get_best_response_graph(number_of_games, number_of_agents, supply, demand)
    number_of_profiles = len(DG.nodes())
    pos = nx.circular_layout(DG)
    labels = dict((n, r'$WE^{' + str(n.count('WE')) + '}WF^{' + str(n.count('WF')) + '}$') for n in DG.nodes())
    fig = plt.figure(3, figsize=(number_of_profiles + 1,number_of_profiles + 1))
    nx.draw(DG, pos, labels = labels, node_size = 2500, font_color = 'white', node_color = 'blue')
    plt.axis('off')
    plt.title(r'Mean Sample Best Response Graph, $\tilde{B}_5^2(\vec{' + str(number_of_games) + '})$. \n Strategy set $\{WE, WF\}$, ' + str(number_of_profiles - 1) + ' agents. \n Impressions = ' + str(supply) + ', demand factor = ' + demand + '.')
    # If the next line is uncommented, we save for EXACTLY number_of_games.
    #plt.savefig('../' + str(number_of_games) + '/bestresponsegraphs/' + 'bestresponsegraphWEWF-' + demand.replace('.','_') + '-' + supply + '-' + str(number_of_profiles - 1) + '.png')
    plt.savefig(setup.create_dir('../' + str(number_of_games) + '/meanbestresponsegraphs/' + str(supply) + '/' + demand + '/') + 'meanbestresponsegraphWEWF-' + str(number_of_profiles - 1) + '-agents.png', bbox_inches='tight')
    #plt.show()
    plt.close(fig)
        
def plot_proportion_pure_nash(number_of_games, demand_factor, impressions, dict_of_pure_nash):
    """
    Given a dictionary: {n : pure nash}, plots propportions
    """
    WE_proportion = [((1.0 / (len(eq) * i)) * sum(we for we, wf in eq)) for i, eq in dict_of_pure_nash.items()]
    WF_proportion = [((1.0 / (len(eq) * i)) * sum(wf for we, wf in eq)) for i, eq in dict_of_pure_nash.items()]
    xaxis = [i for i in range(2,21)]
    fig = plt.figure(4, figsize=(10,4))
    plt.plot(xaxis, WE_proportion, label = 'WE proportion')
    plt.plot(xaxis, WF_proportion, label = 'WF proportion')
    plt.legend()
    plt.title('Proportion of agents playing each strategy at equilibirum\n Demand factor ' + str(demand_factor) + ', impressions ' + str(impressions))
    plt.xlabel('Number of agents')
    plt.ylabel('Proportion at equilibrium')
    plt.savefig('../' + str(number_of_games) + '/proportionsateq/proportionateq-'+demand_factor.replace('.','_')+'-'+impressions+'.png')
    plt.close(fig)

def plot_all_proportion_pure_nash(number_of_games):
    """
    Save all the best response graphs to an image and the
    image of the proportion of pure nash
    """
    for (demand, supply) in setup.get_grid_demand_impressions():
        print('Proportion Graphs Pure Nash for (demand, supply) = (' , demand, ',', supply, ')')
        dir_location = setup.get_agent_dir_location(number_of_games, supply, demand)
        dict_of_pure_nash = mean_best_response_graphs.get_dict_of_pure_nash(number_of_games, demand, supply, dir_location)
        plot_proportion_pure_nash(number_of_games, demand, supply, dict_of_pure_nash)
    
def plot_all_mean_best_response_graphs(number_of_games):
    """
    Wrapper to plot all best response graphs
    """     
    for (supply, demand) in setup.get_grid_supply_demand():
        print('Best Response Graphs for (supply, demand) = (' , supply, ',', demand, ')')
        for i in range(11, 21):
            print('Number of agents = ', i)
            plot_mean_best_response_graph(number_of_games, i, supply, demand)
