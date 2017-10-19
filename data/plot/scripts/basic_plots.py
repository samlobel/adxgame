#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Mon Sep 18 18:54:45 2017

@author: enriqueareyan
"""

import pandas as pd
import numpy as np
import scipy as sp
import scipy.stats
from matplotlib import pyplot as plt
from itertools import combinations, product

def mean_confidence_interval(data, confidence=0.95):
    """
    Compute the confidence interval of the given data (array).
    """
    a = 1.0*np.array(data)
    n = len(a)
    m, se = np.mean(a), scipy.stats.sem(a)
    h = se * sp.stats.t._ppf(( 1 + confidence) / 2., n - 1)
    return m, m-h, m+h

def get_data(dir_location, file_name):
    """
    Reads the data produced by the game. 
    The data contains the following columns:
        game, agent, segment, reach, reward, wincount, wincost.
    In principle this data is enough to compute any statistic
    about the performance of an agent in the game.
    """
    data = pd.read_csv(dir_location + file_name, header = None)
    data.columns = ['game','agent','segment','reach','reward','wincount','wincost']
    data.loc[data['wincount'] >= data['reach'], 'activate'] = 1
    data.loc[data['wincount'] <  data['reach'], 'activate'] = 0
    data.loc[data['wincount'] >= data['reach'], 'value'] = data['reward']
    data.loc[data['wincount'] < data['reach'], 'value'] = 0
    data['effectivereward'] = data['reward'] * data['activate']
    data['profit'] = data['effectivereward'] - data['wincost']
    return data

def get_group_data(dir_location, agent1, agent2, agent1fix = True, max_number_agents = 20):
    """
    Computes aggregates of the data produced by the game.
    Specifically, this function returns the means of 
    profit, activate, wincost and value for each of the
    agents received as a parameter.
    """
    all_data = []
    agent1_data = []
    agent2_data = []
    agent1_means = {}
    agent2_means = {}
    columns = ['profit','activate','wincost', 'value']
    for column in columns: 
        agent1_means[column] = []
        agent2_means[column] = []
    for i in range(0,max_number_agents):
        if agent1fix:
            all_data.append(get_data(dir_location, agent1 + agent2 + '(1-'+str(i+1)+').csv'))
        else:
            all_data.append(get_data(dir_location, agent1 + agent2 + '('+str(i+1)+'-1).csv'))
        agent1_data.append(all_data[i][all_data[i]['agent'].str.contains(agent1 + 'Agent')])
        agent2_data.append(all_data[i][all_data[i]['agent'].str.contains(agent2 + 'Agent')])
        for column in columns:
            agent1_means[column].append(mean_confidence_interval(agent1_data[i][column]))
            agent2_means[column].append(mean_confidence_interval(agent2_data[i][column]))
    return (agent1_means, agent2_means)

def plotmeans(xaxis, xlabel, ylabel, agent1, agent2, agent1_means, agent2_means, ax, addHorizontalLine = False):
    """
    Plots the data of one dimension (e.g., profit)
    as produced by get_group_data()
    """
    y_agent1 = [x for (x,y,z) in agent1_means]
    lb_agent1 = [y for (x,y,z) in agent1_means]
    ub_agent1 = [z for (x,y,z) in agent1_means]
    ax.plot(xaxis, y_agent1, '--', label = agent1, color = 'navy')
    ax.fill_between(xaxis, lb_agent1, ub_agent1, alpha=0.5)
    ax.legend()
    
    y_agent2 = [x for (x,y,z) in agent2_means]
    lb_agent2 = [y for (x,y,z) in agent2_means]
    ub_agent2 = [z for (x,y,z) in agent2_means]    
    ax.plot(xaxis, y_agent2, '--', label = agent2, color = 'darkgreen')
    ax.fill_between(xaxis, lb_agent2, ub_agent2, alpha=0.5, color = 'green')

    if(addHorizontalLine):
        ax.axhline(y = 0, color = 'red')
    ax.legend()
    plt.xlabel(xlabel)
    ax.set_ylabel(ylabel)


def plotGroup(dir_location, image_prefix, demand_factor, impressions, agent1, agent2, fixagent1, max_number_agents = 20):
    """
    Plots all relevant plots by repeatingly calling function 
    plotmeans.
    """
    data = get_group_data(dir_location, agent1, agent2, fixagent1)    
    fig, ax = plt.subplots(nrows=4, ncols=1, sharex=True)
    xaxis = [x for x in range(2,max_number_agents + 2)]
    xlabel = 'Number of agents'
    plotmeans(xaxis, xlabel, 'Activation', agent1, agent2, data[0]['activate'], data[1]['activate'], ax[0], True)
    plotmeans(xaxis, xlabel, 'Value', agent1, agent2, data[0]['value'], data[1]['value'], ax[1])
    plotmeans(xaxis, xlabel, 'Cost', agent1, agent2, data[0]['wincost'], data[1]['wincost'], ax[2])
    plotmeans(xaxis, xlabel, 'Profit', agent1, agent2, data[0]['profit'], data[1]['profit'], ax[3], True)
    ax[0].set_title('Performance comparison, fixing one ' + (agent1 if fixagent1 else agent2) + ' agent. \n Demand factor = '+demand_factor+', impressions = ' + impressions + '.')
    plt.savefig('../' + image_prefix + (agent1+'v'+agent2 if fixagent1 else agent2+'v'+agent1) + '.png')

def produce_all_plots(dir_location, image_prefix, demand_factor, impressions):
    """
    Plots varying the composition of the game, i.e., 
    type and number of agents
    """
    for x in combinations(['SI','WE','WF'], 2):
        for y in [True, False]:
            print('Graph: ' + str(x) + (', Fix Agent ' + str(x[0]) if y else ', Fix Agent ' + str(x[1])))
            plotGroup(dir_location, image_prefix, demand_factor, impressions, x[0], x[1], y) 
    
def produce_latex_and_plots():
    """
    Produces all the latex to show results
    """
    demand_factors = ['0.25','0.75','1.25','3.0']
    impressions = ['2000']
    
    for (demand, supply) in product(demand_factors, impressions):
        dir_location = '../../results/' + demand + '-' + supply + '/agents/'
        image_prefix = 'demand-factor-' + demand.replace('.','_') + '-' + supply + 'impressions-'
        produce_all_plots(dir_location, image_prefix, demand, supply)    
        latex = """
    \\newpage
    \subsection*{Demand Factor """+demand+""", """+supply+""" impressions}
    \hspace*{-1in}
    \includegraphics[scale=0.65]{/home/eareyanv/workspace/adxgame/data/plot/""" + image_prefix + """SIvWE}
    \includegraphics[scale=0.65]{/home/eareyanv/workspace/adxgame/data/plot/""" + image_prefix + """SIvWF}
    
    \hspace*{-1in}
    \includegraphics[scale=0.65]{/home/eareyanv/workspace/adxgame/data/plot/""" + image_prefix + """WEvSI}
    \includegraphics[scale=0.65]{/home/eareyanv/workspace/adxgame/data/plot/""" + image_prefix + """WEvWF}
    
    \hspace*{-1in}
    \includegraphics[scale=0.65]{/home/eareyanv/workspace/adxgame/data/plot/""" + image_prefix + """WFvSI}
    \includegraphics[scale=0.65]{/home/eareyanv/workspace/adxgame/data/plot/""" + image_prefix + """WFvWE}
        """
        print(latex)
        