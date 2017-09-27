#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Tue Sep 26 21:53:16 2017

@author: enriqueareyan
"""

from plots import get_data, mean_confidence_interval, plotmeans
from matplotlib import pyplot as pl
agent1_data = []
agent2_data = []
agent1 = 'SI'
agent2 = 'WE'
all_data = []
agent1_means = {}
agent2_means = {}
columns = ['profit','activate','wincost']
for column in columns: 
    agent1_means[column] = []
    agent2_means[column] = []


for i in range(0,100):
    print(i)
    all_data.append(get_data('../results-varying-impressions/SIWE('+str((i+1)*100)+').csv'))
    agent1_data.append(all_data[i][all_data[i]['agent'].str.contains(agent1 + 'Agent')])
    agent2_data.append(all_data[i][all_data[i]['agent'].str.contains(agent2 + 'Agent')])
    for column in columns:
        agent1_means[column].append(mean_confidence_interval(agent1_data[i][column]))
        agent2_means[column].append(mean_confidence_interval(agent2_data[i][column]))


#fig, ax = pl.subplots(nrows=3, ncols=1, sharex=True)
#plotmeans('Profit', agent1, agent2, agent1_means['profit'], agent2_means['profit'], ax[0])

agent1_means = agent1_means['profit']
agent2_means = agent2_means['profit']

x = [x for x in range(0,100)]
y_agent1 = [x for (x,y,z) in agent1_means]
lb_agent1 = [y for (x,y,z) in agent1_means]
ub_agent1 = [z for (x,y,z) in agent1_means]
pl.plot(x,y_agent1, '--', label = agent1, color = 'navy')
pl.fill_between(x, lb_agent1, ub_agent1, alpha=0.5)

y_agent2 = [x for (x,y,z) in agent2_means]
lb_agent2 = [y for (x,y,z) in agent2_means]
ub_agent2 = [z for (x,y,z) in agent2_means]    
pl.plot(x,y_agent2, '--', label = agent2, color = 'darkgreen')
pl.fill_between(x, lb_agent2, ub_agent2, alpha=0.5, color = 'green')



#plotmeans('Activation', agent1, agent2, data[0]['activate'], data[1]['activate'], ax[1])
#plotmeans('Cost', agent1, agent2, data[0]['wincost'], data[1]['wincost'], ax[2])

