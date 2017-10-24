#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Mon Sep 18 18:54:45 2017

@author: enriqueareyan
"""
import setup
import get_data
from matplotlib import pyplot as plt

def plot_agent_means(xaxis, xlabel, ylabel, agent1, agent2, agent1_means, agent2_means, ax, addHorizontalLine = False):
    """
    Plots the data of one dimension (e.g., profit) as produced by get_agent_group_data()
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

def plot_agent_group(number_of_games, dir_location, image_prefix, demand_factor, impressions, agent1, agent2, fixagent1, max_number_agents = 20):
    """
    Plots all relevant plots by repeatingly calling function plotAgentMeans.
    """
    data = get_data.get_agent_group_data(['profit','activate','wincost', 'value'], number_of_games, dir_location, agent1, agent2, fixagent1)
    fig, ax = plt.subplots(nrows = 4, ncols = 1, sharex = True)
    xaxis = [x for x in range(2, max_number_agents + 2)]
    xlabel = 'Number of agents'
    plot_agent_means(xaxis, xlabel, 'Activation', agent1, agent2, data[0]['activate'], data[1]['activate'], ax[0], True)
    plot_agent_means(xaxis, xlabel, 'Value', agent1, agent2, data[0]['value'], data[1]['value'], ax[1])
    plot_agent_means(xaxis, xlabel, 'Cost', agent1, agent2, data[0]['wincost'], data[1]['wincost'], ax[2])
    plot_agent_means(xaxis, xlabel, 'Profit', agent1, agent2, data[0]['profit'], data[1]['profit'], ax[3], True)
    ax[0].set_title('Performance comparison, fixing one ' + (agent1 if fixagent1 else agent2) + ' agent. \n Demand factor = ' + demand_factor + ', impressions = ' + impressions + '.')
    plt.savefig('../' + str(number_of_games) + '/basicplots/' + image_prefix + (agent1 + 'v' + agent2 if fixagent1 else agent2 + 'v' + agent1) + '.png')
    plt.close()
    
def produce_agents_plots(number_of_games, demand_factor, impressions):
    """
    Plots varying the composition of the game, i.e., type and number of agents.
    """
    image_prefix = 'demand-factor-' + demand_factor.replace('.','_') + '-' + impressions + 'impressions-'
    agent_dir_location = setup.get_agent_dir_location(number_of_games, impressions, demand_factor)
    print('\t(games, demand, supply) = (', number_of_games , ',', demand_factor , ',', impressions, ')')
    for x in setup.get_two_agents_combinations():
        for y in [True, False]:
            print('\t\tAgents Graph: ' + str(x) + (', Fix Agent ' + str(x[0]) if y else ', Fix Agent ' + str(x[1])))
            plot_agent_group(number_of_games, agent_dir_location, image_prefix, demand_factor, impressions, x[0], x[1], y) 
    
def produce_all_agents_plots(number_of_games):
    """
    Given the number of games, produces all the plots.
    """
    print('Plotting market maker plots')
    for (demand, supply) in setup.get_grid_demand_impressions():
        produce_agents_plots(number_of_games, demand, supply)    
    

def produce_all_latex_code(number_of_games):
    """
    Produces all the latex code to show results.
    """
    for (demand, supply) in setup.get_grid_demand_impressions():
        image_prefix = 'demand-factor-' + demand.replace('.','_') + '-' + supply + 'impressions-'
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
        