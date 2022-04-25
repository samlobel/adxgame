#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Mon Oct 23 21:02:12 2017

@author: enriqueareyan
"""

import basic_plots
import marketmaker_plots
import deviation_graphs
import markov_chain

for number_of_games in [100, 200, 400]:
    print('[Plotting for number of games = ', number_of_games, ']')
    basic_plots.produce_all_agents_plots(number_of_games)
    marketmaker_plots.produce_all_market_maker_plots(number_of_games)
    deviation_graphs.plot_all_deviation_graphs(number_of_games)
    deviation_graphs.plot_all_proportion_pure_nash(number_of_games)
    markov_chain.plot_all_soft_deviation_graph(number_of_games)
    markov_chain.plot_all_soft_expected_agents_pure_nash(number_of_games)
