#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Fri Sep 29 11:56:26 2017

@author: eareyanv
"""

# Plots varying the reserve price
from basic_plots import get_data,mean_confidence_interval
from matplotlib import pyplot as plt
from progress import update_progress


def plot_market_market_revenue_by_reserve(dir_location, demand_factor, impressions, number_WE, number_WF):
    """
    Given the number of WE agents and the number of WF agents,
    produces a plot of the market maker revenue as a function of 
    the reserve price.
    """
    cost_data = []
    for r in range(0,131):
        update_progress(r / 130.0)
        data = get_data(dir_location, 'WEWF(' + str(number_WE) + '-' + str(number_WF) + ')-r('+str(r)+').csv')
        cost_data.append(mean_confidence_interval(data.wincost))
    y_agent1 = [x * (number_WE + number_WF) for (x,y,z) in cost_data]
    lb_agent1 = [y * (number_WE + number_WF) for (x,y,z) in cost_data]
    ub_agent1 = [z * (number_WE + number_WF) for (x,y,z) in cost_data]
    xaxis = [0.01 + 0.01*x for x in range(0,131)]
    plt.plot(xaxis, y_agent1, '--', label = 'revenue', color = 'navy')
    plt.fill_between(xaxis, lb_agent1, ub_agent1, alpha=0.5)
    plt.ylabel('Market maker mean revenue')
    plt.xlabel('Reserve price')
    plt.title('Market maker revenue as a function of reserve, ' + str(number_WE) + ' WE, ' + str(number_WF) + 'WF. \n Demand factor = '+demand_factor+', impressions = ' + impressions + '.')
    plt.tight_layout()
    plt.savefig('../' + 'MarketMakerRevenueWE('+ str(number_WE) +')WF('+ str(number_WF) +')-' + demand_factor.replace('.','_') + '-' + impressions + '.png')

number_WE = 4
number_WF = 4
demand_factor = '0.25'
impressions = '2k'
dir_location = '../../results' + demand_factor + '-' + impressions + '-' + str(number_WE + number_WF) + 'agents-varyreserve/'

plot_market_market_revenue_by_reserve(dir_location, demand_factor, impressions, number_WE, number_WF)