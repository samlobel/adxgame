#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Mon Oct 23 17:24:38 2017

@author: enriqueareyan
"""
import get_data
import setup
from matplotlib import pyplot as plt

def plot_market_maker_means(xaxis, xlabel, ylabel, means, ax, addHorizontalLine = False):
    """
    Plots the data of one dimension (e.g., profit) as produced by get_agent_group_data()
    """
    y = [x for (x,y,z) in means]
    lb = [y for (x,y,z) in means]
    ub = [z for (x,y,z) in means]
    ax.plot(xaxis, y, '--', label = ylabel)
    ax.fill_between(xaxis, lb, ub, alpha=0.5)
    ax.legend()
    plt.xlabel(xlabel)
    ax.set_ylabel('Impressions')

def plot_market_maker_group(number_of_games, dir_location, image_prefix, demand_factor, impressions, agent1, agent2, fixagent1, max_number_agents = 20):
    """
    Plots all relevant plots by repeatingly calling function plotMarketMakerMeans.
    """
    data = get_data.get_market_maker_group_data(['reserveTooHigh', 'noBids', 'allocatedAtReserve', 'allocatedNotAtReserve'], number_of_games,  dir_location, agent1, agent2, fixagent1, max_number_agents)
    fig, ax = plt.subplots(nrows = 1, ncols = 1, sharex = True)
    xaxis = [x for x in range(2, max_number_agents + 2)]
    xlabel = 'Number of agents'
    plot_market_maker_means(xaxis, xlabel, 'No Bids', data['noBids'], ax)
    plot_market_maker_means(xaxis, xlabel, '@ Reserve', data['allocatedAtReserve'], ax)
    plot_market_maker_means(xaxis, xlabel, 'Not @ Reserve', data['allocatedNotAtReserve'], ax)
    ax.set_title('Performance comparison, fixing one ' + (agent1 if fixagent1 else agent2) + ' agent, varying ' + (agent1 if not fixagent1 else agent2) + ' agents. \n Demand factor = ' + demand_factor + ', impressions = ' + impressions + '.')
    plt.savefig('../' + str(number_of_games) + '/marketmakerplots/' + image_prefix + (agent1 + 'v' + agent2 if fixagent1 else agent2 + 'v' + agent1) + '.png')
    plt.close()
    
    
def produce_market_maker_plots(number_of_games, demand_factor, impressions):
    """
    Plots varying the composition of the game, i.e., type and number of agents.
    """
    image_prefix = 'demand-factor-' + demand_factor.replace('.','_') + '-' + impressions + 'impressions-'
    market_maker_dir_location = setup.get_market_maker_dir_location(number_of_games, impressions, demand_factor)
    print('(games, demand, supply) = (', number_of_games, ',', demand_factor , ',', impressions, ')')
    for x in setup.get_two_agents_combinations():
        for y in [True, False]:
            print('\t\tMarket Maker Graph: ' + str(x) + (', Fix Agent ' + str(x[0]) if y else ', Fix Agent ' + str(x[1])))
            plot_market_maker_group(number_of_games, market_maker_dir_location, image_prefix, demand_factor, impressions, x[0], x[1], y) 

def produce_all_market_maker_plots(number_of_games):
    print('Plotting market maker plots')
    for (demand, supply) in setup.get_grid_demand_impressions():
        print(demand, supply)
        produce_market_maker_plots(number_of_games, demand, supply)
    