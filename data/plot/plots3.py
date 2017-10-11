#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Fri Sep 29 11:56:26 2017

@author: eareyanv
"""

# Plots varying the reserve price
from plots import get_data,mean_confidence_interval
from matplotlib import pyplot as pl

number_agents1 = 0
number_agents2 = 8
cost_data = []
for i in range(0,130):
    data = get_data('../results-varying-reserve/', 
                    'WEWF(' + str(number_agents1) + ',' + str(number_agents2) + ')-'+str(i)+'.csv')
    cost_data.append(mean_confidence_interval(data.wincost))
    
y_agent1 = [x for (x,y,z) in cost_data]
lb_agent1 = [y for (x,y,z) in cost_data]
ub_agent1 = [z for (x,y,z) in cost_data]
xaxis = [0.01 + 0.01*x for x in range(0,130)]
pl.plot(xaxis, y_agent1, '--', label = 'revenue', color = 'navy')
pl.fill_between(xaxis, lb_agent1, ub_agent1, alpha=0.5)
pl.ylabel('Market maker revenue')
pl.xlabel('Reserve price')
pl.title('Market maker revenue as a function of reserve, ' + str(number_agents1) + ' WE, ' + str(number_agents2) + 'WF')
pl.savefig('../plot/' + 'WEWF_' + str(number_agents1) + '_' + str(number_agents2) + '_vMarketRevenue.png')
