#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Mon Oct 23 17:32:08 2017

@author: enriqueareyan
"""
import pandas as pd
import numpy as np
import scipy as sp
import scipy.stats
import zipfile

def mean_confidence_interval(data, confidence=0.95):
    """
    Compute the confidence interval of the given data (array).
    """
    a = 1.0 * np.array(data)
    n = len(a)
    m, se = np.mean(a), scipy.stats.sem(a)
    h = se * sp.stats.t._ppf(( 1 + confidence) / 2., n - 1)
    return m, m-h, m+h

def zip_contains_file(zip_location, file_location):
    """
    Given a profile configuration, returns true if the profile
    was sampled using number_of_games or false otherwise
    """
    zf = zipfile.ZipFile(zip_location)
    return file_location in zf.namelist()

def get_raw_data(zip_location, file_location):
    """
    Reads the raw data produced by the game.
    """
    print('\t Getting raw data from file ', file_location)
    print('\t\t zip:', zip_location)
    zf = zipfile.ZipFile(zip_location)
    data = pd.read_csv(zf.open(file_location), header = None, error_bad_lines = False)
    return data    

def get_agent_data(zip_location, file_location):
    """
    Reads and process the data produced by the game from the agents point of view. 
    The agent data contains the following columns:
        game, agent, segment, reach, reward, wincount, wincost.
    In principle this data is enough to compute any statistic
    about the performance of an agent in the game.
    """
    data = get_raw_data(zip_location, file_location)
    data.columns = ['game','agent','segment','reach','reward','wincount','wincost']
    data.loc[data['wincount'] >= data['reach'], 'activate'] = 1
    data.loc[data['wincount'] <  data['reach'], 'activate'] = 0
    data.loc[data['wincount'] >= data['reach'], 'value'] = data['reward']
    data.loc[data['wincount'] < data['reach'], 'value'] = 0
    data['effectivereward'] = data['reward'] * data['activate']
    data['profit'] = data['effectivereward'] - data['wincost']
    return data

def get_market_maker_data(number_of_games, dir_location, file_name):
    """
    Reads the data produced by the game from the market maker point of view.
    """
    data = get_raw_data(number_of_games, dir_location, file_name)
    data.columns = ['game', 'reserveTooHigh', 'noBids', 'allocatedAtReserve', 'allocatedNotAtReserve']
    return data
    
def get_agent_group_data(columns, number_of_games, dir_location, agent1, agent2, agent1fix = True, max_number_agents = 20):
    """
    Computes aggregates of the data produced by the game from the point of view of the agent.
    Specifically, this function returns the means of each of the columns received in columns, 
    for each of the agents received as a parameter. The means are computed by aggregating the
    scores of agents of the same type. (see str.contains)
    """
    all_data = []
    agent1_data = []
    agent2_data = []
    agent1_means = {}
    agent2_means = {}
    for column in columns: 
        agent1_means[column] = []
        agent2_means[column] = []
    for i in range(0, max_number_agents):
        if agent1fix:
            all_data.append(get_agent_data(number_of_games, dir_location, agent1 + agent2 + '(1-'+str(i+1)+').csv'))
        else:
            all_data.append(get_agent_data(number_of_games, dir_location, agent1 + agent2 + '('+str(i+1)+'-1).csv'))
        agent1_data.append(all_data[i][all_data[i]['agent'].str.contains(agent1 + 'Agent')])
        agent2_data.append(all_data[i][all_data[i]['agent'].str.contains(agent2 + 'Agent')])
        for column in columns:
            agent1_means[column].append(mean_confidence_interval(agent1_data[i][column]))
            agent2_means[column].append(mean_confidence_interval(agent2_data[i][column]))
    return (agent1_means, agent2_means)

def get_market_maker_group_data(columns, number_of_games, dir_location, agent1, agent2, agent1fix = True, max_number_agents = 20):
    """
    Computes aggregates of the data produced by the game from the point of view of the market maker.
    """
    all_data = []
    means = {}
    for column in columns: 
        means[column] = []
    for i in range(0, max_number_agents):
        if agent1fix:
            all_data.append(get_market_maker_data(number_of_games, dir_location, agent1 + agent2 + '(1-'+str(i+1)+').csv'))
        else:
            all_data.append(get_market_maker_data(number_of_games, dir_location, agent1 + agent2 + '('+str(i+1)+'-1).csv'))
        for column in columns:
            means[column].append(mean_confidence_interval(all_data[i][column]))
    return means
    