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
from itertools import product
from deviation_graphs import produce_profile_data, produce_deviation_graph, get_pure_nash


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
    plt.savefig('../' + 'MarketMakerRevenueWEWF('+ str(number_WE) +'-'+ str(number_WF) +')-' + demand_factor.replace('.','_') + '-' + impressions + '.png')
    plt.close()

def produce_all_reserve_plots():
    number_agents = [0, 4, 8]
    demand_factors = ['0.25','0.75','1.25','3.0']
    impressions = ['2000']
    for (number_WE, number_WF) in product(number_agents, number_agents):
        if(number_WE + number_WF == 8):
            for (demand, supply) in product(demand_factors, impressions):
                print('WEWF(' + str(number_WE) + '-' + str(number_WF) + ')' + '-' + demand + '-' +supply)
                dir_location = '../../results/8-agents-varyreserve-' + demand + '-' + supply + '/agents/'
                plot_market_market_revenue_by_reserve(dir_location, demand, supply, number_WE, number_WF)

def plot_all_reserve_at_eq_plots():
    number_agents = 8
    demand_factors = ['0.25','0.75','1.25','3.0']
    impressions = ['2000']
    for (demand, supply) in product(demand_factors, impressions):
        dir_location = '../../results/' + str(number_agents) +'-agents-varyreserve-' + demand + '-' + supply + '/agents/'
        profit_reserve_at_equilibirum = {}
        for r in range(0,131):
            update_progress(r / 130.0)
            data = produce_profile_data(dir_location, number_agents, r , True)
            DG = produce_deviation_graph(data)
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
        plt.title('Market maker revenue as a function of reserve, \n where ' + str(number_agents) + ' players play at equilibirum. \n Demand factor = ' + demand +', impressions = ' + supply + '.')
        plt.xlabel('Reserve price')
        plt.ylabel('Market maker mean revenue')
        plt.tight_layout()
        plt.savefig('../MarketRevenueAtEq-' + str(number_agents) + 'Agents-' + demand.replace('.','_') + '-' + supply + '.png')
        plt.close()                