#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Fri Sep 29 11:56:26 2017

@author: eareyanv
"""
from matplotlib import pyplot as plt
import reserve_analysis
import setup

def plot_reserve_revenue_at_equilibirum(number_of_games, number_of_agents, supply, demand):    
    revenue_data = [reserve_analysis.get_worst_sink_equilibria_profile(number_of_games, number_of_agents, supply, demand, i) for i in range(0,131)]
    revenue_mean = [x['revenue_mean'] for x in revenue_data]
    revenue_lb = [x['revenue_lb'] for x in revenue_data]
    revenue_ub = [x['revenue_ub'] for x in revenue_data]

    xaxis = [0.01 + 0.01*x for x in range(0,131)]
    plt.plot(xaxis, revenue_mean, label = r'$\delta = ' + str(demand) + '$')
    plt.fill_between(xaxis, revenue_lb, revenue_ub, alpha = 0.5)
    plt.xlabel('Reserve Price')
    plt.ylabel('Market Maket Revenue')
    plt.title('Market maker revenue as a function of reserve prices \n when 8 players play at equilibrium. \n Impressions = ' + str(supply) + '.')
    plt.legend()
    plt.savefig('../../plot/' + str(number_of_games) + '/marketmaker/' + str(supply) + '-' + str(demand) + 'marketmaketrevenueateq.png', bbox_inches='tight')
    
def plot_all_reserve_revenue_at_equilibirum(number_of_games):
    for supply, demand in setup.get_grid_supply_demand():
        plot_reserve_revenue_at_equilibirum(number_of_games, 8, supply, float(demand))