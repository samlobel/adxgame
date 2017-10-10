#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Tue Oct 10 08:50:01 2017

@author: eareyanv
"""

import networkx as nx
import matplotlib.pyplot as plt
from plots import get_data

dir_location = '../results0.25-2k-newreward/'
image_prefix = 'demand-factor-0.25-2kimpressions-newreward-'

#dir_location = '../results3.0-2k-newreward/'
#image_prefix = 'demand-factor-3.0-2kimpressions-newreward-'

def produce_graph_data(number_of_agents):
    """
    Given the number of agents, produces a list of tuples:
        (Strategy profile, (Profit WE, Profit WF))
    An strategy profile indicates the strategy played by each
    player in the game, e.g., a profile WEWEWF indicates a 
    game where two players play WE and one player plays WF.
    Note that this game is symmetric and thus, WEWEWF = WEWFWE= WFWEWE.
    For simplicity, we normalize by first indicating WEs and then WFs.
    """
    graph_data = []
    allWE = get_data(dir_location, 'WE(' + str(number_of_agents) + ').csv')
    graph_data.append(('WE' * number_of_agents, (allWE.profit.mean(), 0)))
    
    for i in range(1,number_of_agents):
        file_location = 'WEWF(' + str(number_of_agents - i) + '-' + str(i) + ').csv'
        mix = get_data(dir_location, file_location)
        WEdata = mix[mix.agent.str.contains('WEAgent')]
        WFdata = mix[mix.agent.str.contains('WFAgent')]
        graph_data.append(('WE' * (number_of_agents - i) + 'WF' * i, (WEdata.profit.mean(), WFdata.profit.mean())))
    
    allWF = get_data(dir_location, 'WF(' + str(number_of_agents) + ').csv')
    graph_data.append(('WF' * number_of_agents, (0, allWF.profit.mean())))
    return graph_data

def produce_deviation_graph(graph_data):
    """
    Given the data of the graph, produces and plots a 
    DG (directed graph) object from the library networkx.
    """
    number_of_agents = len(graph_data)
    DG = nx.DiGraph()
    DG.add_nodes_from([v[0] for v in graph_data])
    for i in range(1,number_of_agents):
        print(i)
        DG.add_node(graph_data[i][0])
        # WE deviating to WF, i.e, is a WE making less than a WF?
        if graph_data[i-1][1][0] < graph_data[i][1][1]:
            DG.add_edge(graph_data[i-1][0], graph_data[i][0])
        # WF deviating to WE, i.e, is a WF making less than a WE?
        if graph_data[i][1][1] < graph_data[i-1][1][0]:
            DG.add_edge(graph_data[i][0], graph_data[i-1][0])
    pos = nx.circular_layout(DG)
    labels = dict((i, r'$WE^{' + str(i.count('WE')) + '}WF^{' + str(i.count('WF')) + '}$') for (i, v) in graph_data)
    plt.figure(3,figsize=(number_of_agents + 1,number_of_agents + 1))
    nx.draw(DG, pos, labels = labels, node_size = 2500, font_color = 'white', node_color = 'blue')
    plt.savefig("graph.png")
    plt.show()
    
    # Get all the pure Nash reduces to getting all the
    # 'sink' nodes (actaully, leaf nodes, since this special
    # graph is just a tree).
    # This is a list of tuples (#WE,#WF)
    pure_nash = [(i.count('WE'), i.count('WF')) for i in DG.nodes_iter() if DG.out_degree(i)==0]
    return pure_nash
    
number_of_agents = 3
graph_data = produce_graph_data(number_of_agents)
print(produce_deviation_graph(graph_data))


dict_of_pure_nash = {}
for i in range(2, 21):
    print('Number of agents: ' + str(i))
    dict_of_pure_nash[i] = produce_deviation_graph(produce_graph_data(i))
    
WE_proportion = [((1.0 / (len(eq) * i)) * sum(we for we,wf in eq)) for i, eq in dict_of_pure_nash.items()]
WF_proportion = [((1.0 / (len(eq) * i)) * sum(wf for we,wf in eq)) for i, eq in dict_of_pure_nash.items()]
xaxis = [i for i in range(2,21)]
plt.plot(xaxis, WE_proportion, label = 'WE proportion')
plt.plot(xaxis, WF_proportion, label = 'WF proportion')
plt.legend()
plt.xlabel('Number of agents')
plt.ylabel('Proportion at equilibrium')

    
    
    