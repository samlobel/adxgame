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

"""data100 = pd.read_csv('SIWE(1-1)-100.csv', header = None)
data100.columns = ['game','agent','segment','reach','reward','wincount','wincost']
data100.loc[data100['wincount'] >= data100['reach'], 'activate'] = 1
data100.loc[data100['wincount'] <  data100['reach'], 'activate'] = 0
data100['effectivereward'] = data100['reward'] * data100['activate']

data100['profit'] = data100['effectivereward'] - data100['wincost']
data100WE = data100[data100['agent'] == 'WEAgent0']
data100SI = data100[data100['agent'] == 'SIAgent0']"""

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

data = pd.read_csv('SIWE(1-1).csv', header = None)

#data = pd.read_csv('SIWF(3-3).csv', header = None)
#data = pd.read_csv('allWE3.csv', header = None)
#data = pd.read_csv('allWF3.csv', header = None)
#data = pd.read_csv('WEWF(1-4).csv', header = None)


all_data = []
agent1_data = []
agent2_data = []
agent1_means = []
agent2_means = []
agent1 = 'WE'
agent2 = 'WF'
for i in range(0,10):
    #all_data.append(get_data(agent1 + agent2 + '(1-'+str(i+1)+').csv'))
    all_data.append(get_data(agent1 + agent2 + '('+str(i+1)+'-1).csv'))
    agent1_data.append(all_data[i][all_data[i]['agent'].str.contains(agent1 + 'Agent')])
    agent2_data.append(all_data[i][all_data[i]['agent'].str.contains(agent2 + 'Agent')])
    agent1_means.append(mean_confidence_interval(agent1_data[i].profit))
    agent2_means.append(mean_confidence_interval(agent2_data[i].profit))
    

# Plot a graph with confidence interval
x = [x for x in range(2,12)]
#pl.xticks(x)

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
pl.legend()
pl.xlabel('Number of agents')
pl.ylabel('Average profit')
pl.title('Revenue comparison, fixing one ' + agent2 + ' agent')
pl.show()