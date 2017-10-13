#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Tue Oct 10 08:50:01 2017

@author: eareyanv
"""

import networkx as nx
import matplotlib.pyplot as plt
from basic_plots import get_data, mean_confidence_interval
from itertools import product
from progress import update_progress

def produce_graph_data(dir_location, number_of_agents, reserve = 0, include_reserve = False):
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
    for i in range(0,number_of_agents + 1):
        file_location = 'WEWF(' + str(number_of_agents - i) + '-' + str(i) + ')' + ('-r(' + str(reserve) + ')' if include_reserve else '')+'.csv'
        mix = get_data(dir_location, file_location)
        WEdata = mix[mix.agent.str.contains('WEAgent')]
        WFdata = mix[mix.agent.str.contains('WFAgent')]
        graph_data.append(('WE' * (number_of_agents - i) + 'WF' * i, (WEdata.profit.mean(), WFdata.profit.mean())))
    return graph_data

def produce_deviation_graph(demand_factor, impressions, graph_data):
    """
    Given the data of the graph, produces a directed graph, DG,
    whichi is an object of the library networkx.
    """
    number_of_agents = len(graph_data)
    DG = nx.DiGraph()
    DG.add_nodes_from([v[0] for v in graph_data])
    for i in range(1, number_of_agents):
        DG.add_node(graph_data[i][0])
        # WE deviating to WF, i.e, is a WE making less than a WF?
        if graph_data[i-1][1][0] < graph_data[i][1][1]:
            DG.add_edge(graph_data[i-1][0], graph_data[i][0])
        # WF deviating to WE, i.e, is a WF making less than a WE?
        if graph_data[i][1][1] < graph_data[i-1][1][0]:
            DG.add_edge(graph_data[i][0], graph_data[i-1][0])
    return DG

def get_pure_nash(DG):
    """Get all the pure Nash reduces to getting all the
    'sink' nodes (actually, leaf nodes, since this special
    graph is just a tree). This is a list of tuples (#WE,#WF)"""
    return [(i.count('WE'), i.count('WF')) for i in DG.nodes_iter() if DG.out_degree(i)==0]

def plot_deviation_graph(demand_factor, impressions, graph_data, DG):
    """
    Plots the deviation graph. This function produces
    a circular type of plot of the deviation graph.
    """
    number_of_agents = len(graph_data)
    pos = nx.circular_layout(DG)
    labels = dict((i, r'$WE^{' + str(i.count('WE')) + '}WF^{' + str(i.count('WF')) + '}$') for (i, v) in graph_data)
    fig = plt.figure(3,figsize=(number_of_agents + 1,number_of_agents + 1))
    nx.draw(DG, pos, labels = labels, node_size = 2500, font_color = 'white', node_color = 'blue')
    plt.title('Deviation Graph, WE v WF, ' + str(number_of_agents - 1) + ' agents')
    plt.savefig("deviationgraphWEWF-" + demand_factor.replace('.','_') + "-" + impressions + "-" + str(number_of_agents - 1)+".png")
    plt.close(fig)
        
def plot_proportion_pure_nash(demand_factor, impressions, dict_of_pure_nash):
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
    plt.savefig('proportionateq-'+demand_factor.replace('.','_')+'-'+impressions+'.png')
    plt.close(fig)

def get_dict_of_pure_nash(demand_factor, impressions, dir_location):
    """
    This function calls the function that plots the deviation graph 
    for games with 2 to 20 players. It returns a dict of pure nash.
    """
    dict_of_pure_nash = {}
    for i in range(2, 21):
        print('Number of agents: ' + str(i))
        graph_data = produce_graph_data(dir_location, i)
        DG = produce_deviation_graph(demand_factor, impressions, graph_data)
        # Uncomment next line to bulk produce all graphs
        #plot_deviation_graph(demand_factor, impressions, graph_data, DG)
        dict_of_pure_nash[i] = get_pure_nash(DG)
    return dict_of_pure_nash

def plot_all_proportion_pure_nash():
    """
    Save all the deviation graphs to an image and the
    image of the proportion of pure nash
    """
    demand_factors = ['0.25','3.0']
    impressions = ['2k']
        
    for (demand, supply) in product(demand_factors, impressions):
        dir_location = '../results' + demand + '-' + supply + '-newreward/'
        print(dir_location)
        dict_of_pure_nash = get_dict_of_pure_nash(demand, supply, dir_location)
        plot_proportion_pure_nash(demand, supply, dict_of_pure_nash)

number_agents = 8
demand_factor = '3.0'
impressions = '2k'
dir_location = '../../results' + demand_factor + '-' + impressions + '-' + str(number_agents) + 'agents-varyreserve/'
profit_reserve_at_equilibirum = {}
for r in range(0,131):
    update_progress(r / 130.0)
    data = produce_graph_data(dir_location, number_agents, r , True)
    DG = produce_deviation_graph(0, 2000, data)
    pure_nash = get_pure_nash(DG)
    list_profit = []
    for (we,wf) in pure_nash:
        file_name = 'WEWF(' + str(we) + '-' + str(wf) + ')-r(' + str(r) + ').csv'
        temp = get_data(dir_location, file_name)
        list_profit.append(mean_confidence_interval(temp.wincost))
    profit_reserve_at_equilibirum[r] = min(list_profit, key = lambda t: t[0])

xaxis = [0.01 + 0.01*x for x in range(0,131)]
profit_confidence = list(profit_reserve_at_equilibirum.values())
plt.plot(xaxis, [mean*number_agents for (mean, lb, ub) in profit_confidence], '--', color = 'navy')
plt.fill_between(xaxis, [lb*number_agents for (mean, lb, ub) in profit_confidence], [ub*number_agents for (mean, lb, ub) in profit_confidence], alpha=0.5)
plt.title('Market maker revenue as a function of reserve, \n where ' + str(number_agents) + ' players play at equilibirum. \n Demand factor = ' + demand_factor +', impressions = ' + impressions + '.')
plt.xlabel('Reserve price')
plt.ylabel('Market maker mean revenue')
plt.tight_layout()
plt.savefig('../MarketRevenueAtEq-' + str(number_agents) + 'Agents-' + demand_factor.replace('.','_') + '-' + impressions + '.png')
plt.show()

# For inspection purposes, the next line gets the graph data for a 
# given number of agents
#dir_location = '../results0.25-2k-newreward/'
#graph_data = produce_graph_data(dir_location, number_of_agents = 3)
