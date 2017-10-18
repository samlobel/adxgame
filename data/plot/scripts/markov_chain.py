#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Mon Oct 16 11:10:11 2017

@author: enriqueareyan
"""
from deviation_graphs import produce_profile_data
from discreteMarkovChain import markovChain
import numpy as np
import networkx as nx
from scipy.stats import norm
import matplotlib.pyplot as plt
from networkx.drawing.nx_pydot import write_dot


def normal1BiggerNormal2(mean1, std1, n1, mean2, std2, n2):
    """
    Returns the probability that the sampling distribution
    of the mean (i.e., normal distribution) defined
    by mean1, std1, n1 is bigger than the sampling distribution
    of the mean (i.e., normal distribution) defined
    by mean2, std2, n2.
    """
    return 1.0 - norm.cdf(0, mean1 - mean2, (std1 / np.sqrt(std1)) + (std2 / np.sqrt(std2)))
    
def produce_soft_deviation_graph(profile_data):
    """
    Given the profile data, creates the directed, weighted deviation graph
    """
    number_of_profiles = len(profile_data)
    DG = nx.DiGraph()
    DG.add_nodes_from([v[0] for v in profile_data])
    
    # Boder cases first:
    # Start, all WE deviate to WF
    start_WE_data = profile_data[0][1]['WE']
    move_WF_data =  profile_data[1][1]['WF']
    start_WE_best = normal1BiggerNormal2(start_WE_data['mean'], start_WE_data['std'], start_WE_data['n'], move_WF_data['mean'], move_WF_data['std'], move_WF_data['n'])
    DG.add_edge(profile_data[0][0], profile_data[0][0], label = float("{0:.2f}".format(start_WE_best)))
    DG.add_edge(profile_data[0][0], profile_data[1][0], label = float("{0:.2f}".format(1 - start_WE_best)))
    DG.add_weighted_edges_from([(profile_data[0][0], profile_data[0][0], start_WE_best), (profile_data[0][0], profile_data[1][0], 1 - start_WE_best)])
    # End, all WF deviate to WE
    end_WF_data = profile_data[number_of_profiles-1][1]['WF']
    move_WE_data =  profile_data[number_of_profiles-2][1]['WE']
    end_WF_best = normal1BiggerNormal2(end_WF_data['mean'], end_WF_data['std'], end_WF_data['n'], move_WE_data['mean'], move_WE_data['std'], move_WE_data['n'])
    DG.add_edge(profile_data[number_of_profiles-1][0], profile_data[number_of_profiles-1][0], label = float("{0:.2f}".format(end_WF_best)))
    DG.add_edge(profile_data[number_of_profiles-1][0], profile_data[number_of_profiles-2][0], label = float("{0:.2f}".format(1 - end_WF_best)))
    DG.add_weighted_edges_from([(profile_data[number_of_profiles-1][0], profile_data[number_of_profiles-1][0], end_WF_best), (profile_data[number_of_profiles-1][0], profile_data[number_of_profiles-2][0], 1 - end_WF_best)])
    for i in range(1, number_of_profiles - 1):
        WE_data  = profile_data[i][1]['WE']
        WF_data = profile_data[i][1]['WF']
        backward_WE_data = profile_data[i-1][1]['WE']
        forward_WF_data  = profile_data[i+1][1]['WF']
        current_WF_best = normal1BiggerNormal2(WF_data['mean'], WF_data['std'], WF_data['n'], backward_WE_data['mean'], backward_WE_data['std'], backward_WE_data['n'])
        deviate_to_WE = 1 - current_WF_best
        current_WE_best = normal1BiggerNormal2(WE_data['mean'], WE_data['std'], WE_data['n'], forward_WF_data['mean'], forward_WF_data['std'], forward_WF_data['n'])
        deviate_to_WF = 1 - current_WE_best
        vector = [current_WF_best, current_WE_best, deviate_to_WE, deviate_to_WF]
        normalization_constant = sum(vector)
        normalized_vector = [x / normalization_constant for x in vector]
        DG.add_edge(profile_data[i][0], profile_data[i][0], label = float("{0:.2f}".format(normalized_vector[0]+normalized_vector[1])))
        DG.add_edge(profile_data[i][0], profile_data[i-1][0], label = float("{0:.2f}".format(normalized_vector[2])))
        DG.add_edge(profile_data[i][0], profile_data[i+1][0], label = float("{0:.2f}".format(normalized_vector[3])))
        DG.add_weighted_edges_from([(profile_data[i][0], profile_data[i][0], normalized_vector[0]+normalized_vector[1]), (profile_data[i][0], profile_data[i-1][0], normalized_vector[2]), (profile_data[i][0], profile_data[i+1][0], normalized_vector[3])])
    return DG

def compute_steady_state(DG):
    #Compute the steady state distribution
    P = nx.adjacency_matrix(DG)
    mc = markovChain(P)
    mc.computePi('linear') #We can also use 'power', 'krylov' or 'eigen'
    steady_state = mc.pi
    #print(P.todense())
    
    map_steady_dist = {}
    i = 0
    for profile in DG.nodes():
        map_steady_dist[profile] = steady_state[i]
        i = i + 1
    return map_steady_dist
    
def plot_soft_deviation_graph(DG, map_steady_dist):
    """
    Given a directed graph object DG with weighted edges, 
    plots the soft deviation graph. To produce this graph,
    first computes the steady state distribution.
    """    
    # Drawing edges weights
    edge_labels = edge_labels=dict([((u,v,),float("{0:.2f}".format(d['weight']))) for u,v,d in DG.edges(data=True) if u!=v])
    labels = dict((i, r'$WE^{' + str(i.count('WE')) + '}WF^{' + str(i.count('WF')) + '}$') for (i, v) in profile_data)
    
    pos=nx.circular_layout(DG)
    number_of_profiles = DG.number_of_nodes()
    fig = plt.figure(3, figsize=(number_of_profiles + 3,number_of_profiles + 3))
    for node, prob in map_steady_dist.items():
        print(node,prob)
        nx.draw_networkx_nodes(DG, pos, nodelist=[node], alpha=prob, node_color = 'black', node_size = 5000)
    nx.draw_networkx_edge_labels(DG, pos, edge_labels=edge_labels, label_pos = 0.3)
    nx.draw_networkx_edges(DG, pos)
    nx.draw_networkx_labels(DG, pos, labels, font_size=16, font_color='red')
    #nx.draw(DG, pos,labels = labels, node_size = 2500, font_color = 'white')
    #nx.draw(DG, pos)
    plt.axis('off')
    plt.savefig("../deviationgraphSoftWEWF-" + demand_factor.replace('.','_') + "-" + impressions + "-" + str(number_of_profiles)+".png")
    plt.show()
    plt.close(fig)
    
def expected_number_strategies(map_steady_dist):
    expected_WE = 0
    expected_WF = 0    
    for s,p in map_steady_dist.items():
        expected_WE += s.count('WE') * p
        expected_WF += s.count('WF') * p
    return {'WE': expected_WE, 'WF': expected_WF}

def plot_soft_expected_agents_pure_nash(dir_location, demand_factor, impressions):
    y = [expected_number_strategies(compute_steady_state(produce_soft_deviation_graph(produce_profile_data(dir_location, i)))) for i in range(2,21)]    
    fig = plt.figure(4, figsize=(10,4))
    xaxis = [i for i in range(2,21)]
    plt.plot(xaxis, [x['WE'] for x in y], label = 'Expected # WE')
    plt.plot(xaxis, [x['WF'] for x in y], label = 'Expected # WF')
    plt.legend()
    plt.title('Expected number of agents playing each strategy\n Demand factor ' + str(demand_factor) + ', impressions ' + str(impressions))
    plt.xlabel('Number of players')
    plt.ylabel('Expected number of agents playing strategy.')
    plt.savefig('../proportionateqSoft-'+demand_factor.replace('.','_')+'-'+impressions+'.png')
    plt.show()
    plt.close(fig)


# Testing one soft graph, manually
number_agents = 5
#demand_factor = '0.25'
demand_factor = '3.0'
impressions = '2k'
dir_location = '../../results' + demand_factor + '-' + impressions + '-newreward/'
#profile_data = produce_profile_data(dir_location, number_agents)
#DG = produce_soft_deviation_graph(profile_data)
#write_dot(DG, 'test.dot')
#map_steady_dist = compute_steady_state(DG)
#print(map_steady_dist)
#plot_soft_deviation_graph(DG, map_steady_dist)

plot_soft_expected_agents_pure_nash(dir_location, demand_factor, impressions)

"""# Bulk graph plotting
for i in range(1,20):
    profile_data = produce_profile_data(dir_location, i)
    DG = produce_soft_deviation_graph(profile_data)
    map_steady_dist = compute_steady_state(DG)
    plot_soft_deviation_graph(DG, map_steady_dist)"""
    
    
