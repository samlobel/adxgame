#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Tue Sep 26 21:53:16 2017

@author: enriqueareyan
"""

from plots import get_data, mean_confidence_interval, plotmeans
from matplotlib import pyplot as pl


def get_group_data(dir_location, agent1, agent2):
    agent1_data = []
    agent2_data = []
    all_data = []
    agent1_means = {}
    agent2_means = {}
    columns = ['profit','activate','wincost']
    for column in columns: 
        agent1_means[column] = []
        agent2_means[column] = []
    for i in range(0,100):
        all_data.append(get_data(dir_location, agent1 + agent2 + '('+str((i+1)*100)+').csv'))
        agent1_data.append(all_data[i][all_data[i]['agent'].str.contains(agent1 + 'Agent')])
        agent2_data.append(all_data[i][all_data[i]['agent'].str.contains(agent2 + 'Agent')])
        for column in columns:
            agent1_means[column].append(mean_confidence_interval(agent1_data[i][column]))
            agent2_means[column].append(mean_confidence_interval(agent2_data[i][column]))
    return (agent1_means, agent2_means)

# Plots varying the number of impressions
agent1 = 'WE'
agent2 = 'WF'
data = get_group_data('../results-varying-impressions/', agent1, agent2)
fig, ax = pl.subplots(nrows=3, ncols=1, sharex=True)
xaxis = [(x+1)*100 for x in range(0,100)]
xlabel = 'Number of impressions'
plotmeans(xaxis, xlabel, 'Profit', agent1, agent2, data[0]['profit'], data[1]['profit'], ax[0])
plotmeans(xaxis, xlabel, 'Activation', agent1, agent2, data[0]['activate'], data[1]['activate'], ax[1])
plotmeans(xaxis, xlabel, 'Cost', agent1, agent2, data[0]['wincost'], data[1]['wincost'], ax[2])
ax[0].set_title(agent1 + ' v ' + agent2 + ' as a function of number of impressions')
pl.savefig('../plot/' + agent1+'v'+agent2+'vImpressions.png')