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
from matplotlib import pyplot as pl

def mean_confidence_interval(data, confidence=0.95):
    a = 1.0*np.array(data)
    n = len(a)
    m, se = np.mean(a), scipy.stats.sem(a)
    h = se * sp.stats.t._ppf(( 1 + confidence) / 2., n - 1)
    return m, m-h, m+h

def get_data(file_name):
    data = pd.read_csv(file_name, header = None)
    data.columns = ['game','agent','segment','reach','reward','wincount','wincost']
    data.loc[data['wincount'] >= data['reach'], 'activate'] = 1
    data.loc[data['wincount'] <  data['reach'], 'activate'] = 0
    data['effectivereward'] = data['reward'] * data['activate']
    data['profit'] = data['effectivereward'] - data['wincost']
    return data

def get_group_data(agent1, agent2, agent1fix = True):
    all_data = []
    agent1_data = []
    agent2_data = []
    agent1_means = {}
    agent2_means = {}
    columns = ['profit','activate','wincost']
    for column in columns: 
        agent1_means[column] = []
        agent2_means[column] = []
    for i in range(0,max_number_agents):
        if agent1fix:
            all_data.append(get_data(agent1 + agent2 + '(1-'+str(i+1)+').csv'))
        else:
            all_data.append(get_data(agent1 + agent2 + '('+str(i+1)+'-1).csv'))
        agent1_data.append(all_data[i][all_data[i]['agent'].str.contains(agent1 + 'Agent')])
        agent2_data.append(all_data[i][all_data[i]['agent'].str.contains(agent2 + 'Agent')])
        for column in columns:
            agent1_means[column].append(mean_confidence_interval(agent1_data[i][column]))
            agent2_means[column].append(mean_confidence_interval(agent2_data[i][column]))
    return (agent1_means, agent2_means)
    

def plotmeans(ylabel, agent1, agent2, agent1_means, agent2_means, ax):
    x = [x for x in range(2,max_number_agents + 2)]
    y_agent1 = [x for (x,y,z) in agent1_means]
    lb_agent1 = [y for (x,y,z) in agent1_means]
    ub_agent1 = [z for (x,y,z) in agent1_means]
    ax.plot(x,y_agent1, '--', label = agent1, color = 'navy')
    ax.fill_between(x, lb_agent1, ub_agent1, alpha=0.5)
    ax.legend()
    
    y_agent2 = [x for (x,y,z) in agent2_means]
    lb_agent2 = [y for (x,y,z) in agent2_means]
    ub_agent2 = [z for (x,y,z) in agent2_means]    
    ax.plot(x,y_agent2, '--', label = agent2, color = 'darkgreen')
    ax.fill_between(x, lb_agent2, ub_agent2, alpha=0.5, color = 'green')

    ax.legend()
    pl.xlabel('Number of agents')
    ax.set_ylabel(ylabel)


def plotGroup(agent1, agent2, fixagent1):
    data = get_group_data(agent1, agent2, fixagent1)    
    fig, ax = pl.subplots(nrows=3, ncols=1, sharex=True)
    plotmeans('Profit', agent1, agent2, data[0]['profit'], data[1]['profit'], ax[0])
    plotmeans('Activation', agent1, agent2, data[0]['activate'], data[1]['activate'], ax[1])
    plotmeans('Cost', agent1, agent2, data[0]['wincost'], data[1]['wincost'], ax[2])
    ax[0].set_title('Performance comparison, fixing one ' + (agent1 if fixagent1 else agent2) + ' agent')
    #pl.savefig('../plot/' + (agent1+'v'+agent2+'Version1' if fixagent1 else agent2+'v'+agent1+'Version1') + '.png')
    pl.savefig('../plot/' + (agent1+'v'+agent2+'Version2' if fixagent1 else agent2+'v'+agent1+'Version2') + '.png')

"""from itertools import combinations
max_number_agents = 20

for x in combinations(['SI','WE','WF'], 2):
    for y in [True, False]:
        print('Graph: ' + str(x) + (', Fix Agent ' + str(x[0]) if y else ', Fix Agent ' + str(x[1])))
        plotGroup(x[0],x[1],y)"""
