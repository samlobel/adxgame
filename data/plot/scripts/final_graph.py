#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Fri Oct 27 15:59:00 2017

@author: eareyanv
"""
import pandas as pd
import numpy as np
import networkx as nx
import matplotlib.pyplot as plt
import deviation_graphs
import deviation_analysis
import markov_chain
import setup
from scipy.stats import norm


def normal1BiggerNormal2(mean1, var1, n1, mean2, var2, n2):
    """
    Returns the probability that the sampling distribution
    of the mean (i.e., normal distribution) defined
    by mean1, std1, n1 is bigger than the sampling distribution
    of the mean (i.e., normal distribution) defined
    by mean2, std2, n2.
    """
    return 1.0 - norm.cdf(0, mean1 - mean2, np.sqrt((var1 / n1) + (var2 / n2)))

def add_weight_to_node(edge_dict, node1, node2, weight):
    """
    Given a dictionary, a node1 and node2, adds entry {(node1, node2):weight}
    if the key (node1, node2) does not exists, otherwise, it accumulates (adds) 
    the weigth to the current weight in (node1, node2)
    """
    if(node1 not in edge_dict):
        edge_dict[node1] = {}
    if(node2 not in edge_dict[node1]):
        edge_dict[node1][node2] = weight
    else:        
        edge_dict[node1][node2] = edge_dict[node1][node2] + weight


def construct_final_graph(n1, n2, number_of_agents, supply, demand):
    """
    Given, n1, n2, number_of_agents, supply, and demand, construct the final graph
    where edges for which there is agreement we add an edge with weight one and
    edges for which there is not an agreement we add edges with weight proportional
    to the probability of deviations (difference of the mean sample).
    """
    data = pd.read_csv('../../stability/' + str(n1) + '-' + str(n2) + '/stability-for-' + str(number_of_agents) + '-agents.csv')
    data = data[(data['impressions'] == supply) & (data['demand_factor'] == demand)]
    DG = nx.DiGraph()
    DG.add_nodes_from(['WE' * (number_of_agents - i) + 'WF' * i for i in range(0, number_of_agents + 1)])
    edge_dict = {}
    for index, row in data.iterrows():
        node1 = 'WE' * row['WE1'] + 'WF' * row['WF1']
        node2 = 'WE' * row['WE2'] + 'WF' * row['WF2']
        # If the direction for n1 and n2 agree, construct that edge
        #print(node1, node2)
        if(row['direction_' + str(n1)] == row['direction_' + str(n2)]):
            #print('Agree in direction', row['direction_' + str(n1)])
            #Add forward edge
            if(row['direction_' + str(n1)] == 'f'):
               add_weight_to_node(edge_dict, node1, node2, 1)
            elif(row['direction_' + str(n1)] == 'b'):
               add_weight_to_node(edge_dict, node2, node1, 1)
            else:
                raise ValueError('Direction are the same but not either f or b')
        # Otherwise, construct the markov chain between these two nodes
        else:
            # Get the profile data for the disagreement
            number_of_games_profile_1 = deviation_analysis.determine_cascade_profile(n2, number_of_agents, row['WE1'], row['WF1'], str(supply), str(demand))
            number_of_games_profile_2 = deviation_analysis.determine_cascade_profile(n2, number_of_agents, row['WE2'], row['WF2'], str(supply), str(demand))
            profile1_data = deviation_graphs.get_specific_profile_data(row['WE1'], row['WF1'], number_of_games_profile_1, str(supply), str(demand))
            profile2_data = deviation_graphs.get_specific_profile_data(row['WE2'], row['WF2'], number_of_games_profile_2, str(supply), str(demand))
            # Is the mean of WE in the first profile bigger than the mean of WF in the secod profile?
            probability_WE_bigger = normal1BiggerNormal2(profile1_data[1]['WE']['mean'], profile1_data[1]['WE']['var'], profile1_data[1]['WE']['n'], 
                                                         profile2_data[1]['WF']['mean'], profile2_data[1]['WF']['var'], profile2_data[1]['WF']['n'])
            # Adds the weights obtained by comparing the means of neighboring profiles.
            add_weight_to_node(edge_dict, node1, node2, 1 - probability_WE_bigger)
            add_weight_to_node(edge_dict, node1, node1, probability_WE_bigger)
            add_weight_to_node(edge_dict, node2, node1, probability_WE_bigger)
            add_weight_to_node(edge_dict, node2, node2, 1 - probability_WE_bigger)
            """print('Disagree in direction')
            print('number_of_games_profile_1 =',number_of_games_profile_1,', number_of_games_profile_2 = ', number_of_games_profile_2)
            print(profile1_data[0] , ' WE Mean' ,profile1_data[1]['WE'])
            print(profile2_data[0] , ' WF Mean' ,profile2_data[1]['WF'])
            print(probability_WE_bigger)"""
    # Normalize weights and add edges
    for (node, neighbords) in edge_dict.items():
        normalizer = 0
        for (neigh, weight) in neighbords.items():
            normalizer += weight
        for (neigh, weight) in neighbords.items():
            DG.add_weighted_edges_from([(node, neigh, weight / normalizer)])
    return DG
            
                
def plot_directed_weighted_graph(DG, n1, n2, supply, demand, steady_dist = {}):
    """
    Plots a directed, weighted graph.
    """
    # Compute the edge labels, i.e., the weights (probabilities) of transition
    edge_labels = edge_labels=dict([((u,v,),float("{0:.2f}".format(d['weight']))) for u,v,d in DG.edges(data=True) if u!=v])
    # Compute node labels
    labels = dict((i, r'$WE^{' + str(i.count('WE')) + '}WF^{' + str(i.count('WF')) + '}$') for i in DG.nodes())
    pos=nx.circular_layout(DG)
    number_of_profiles = DG.number_of_nodes()
    fig = plt.figure(3, figsize=(number_of_profiles + 3,number_of_profiles + 3))
    for node in DG.nodes():
        nx.draw_networkx_nodes(DG, pos, nodelist=[node], alpha = (max(steady_dist[node], 0) if steady_dist else 1.0),  node_color = 'black', node_size = 5000)
    nx.draw_networkx_labels(DG, pos, labels, font_size=16, font_color='red')
    nx.draw_networkx_edge_labels(DG, pos, edge_labels=edge_labels, label_pos = 0.3)
    nx.draw_networkx_edges(DG, pos)
    #nx.draw(DG, pos,labels = labels, node_size = 2500, font_color = 'white')
    #nx.draw(DG, pos)
    plt.title('Game Markov Chain for ' + str(number_of_profiles - 1) + ' agents. \n Impressions ' + str(supply) + ', demand factor ' + str(demand) + '. \n (n1,n2) = (' + str(n1) + ','+str(n2) + ')')
    plt.axis('off')
    image_name = '../../markovchains/' + str(n1) + '-' + str(n2) + '/MC-' + str(demand).replace('.','_') + '-' + str(supply) + '-' + str(number_of_profiles - 1) + '.png'
    plt.savefig(image_name)
    #plt.show()
    plt.close(fig)
    return image_name

#def plot_all_directed_wei_graphs(number_of_agents):
#supply = 2000
#demand = 1.25
"""for number_of_agents in range(3,21):
    for demand, supply  in setup.get_grid_demand_impressions():
        pair_of_number_of_games = [(100, 200), (200, 400), (400, 800)]
        #filenames = []
        demand = float(demand)
        supply = int(supply)
        for n1,n2 in pair_of_number_of_games:
            print(n1,n2, number_of_agents, demand, supply)
            DG = construct_final_graph(n1, n2, number_of_agents, supply, demand)
            map_steady_dist = markov_chain.compute_steady_state(DG)
            image_name = plot_directed_weighted_graph(DG, n1, n2, supply, demand, map_steady_dist)
            #print(image_name)
            #filenames.append(image_name)

images = []
for filename in filenames:
    images.append(imageio.imread(filename))
imageio.mimsave('movie.gif', images, duration=0.5)"""
