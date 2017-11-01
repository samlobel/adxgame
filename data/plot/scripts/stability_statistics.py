#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Fri Oct 27 13:20:03 2017

@author: eareyanv
"""
import pandas as pd
import matplotlib.pyplot as plt

def agreements_statistics(number_of_agents):
    """ 
    Given the number of agents, returns a tuple
    (total_number_profiles, stable_profiles_100_200, stable_profiles_200_400, stable_profiles_400_800)
    """
    agreements100_200 = pd.read_csv('../../stability/100-200/stability-for-' + str(number_of_agents) + '-agents.csv')
    agreements200_400 = pd.read_csv('../../stability/200-400/stability-for-' + str(number_of_agents) + '-agents.csv')
    agreements400_800 = pd.read_csv('../../stability/400-800/stability-for-' + str(number_of_agents) + '-agents.csv')
    
    total_profiles = len(agreements100_200)
    if(total_profiles != len(agreements200_400) or total_profiles != len(agreements400_800)):
        raise ValueError('The length of the stability data do not agree')
    stable_profiles_100_200 = len(agreements100_200[agreements100_200['direction_100'] == agreements100_200['direction_200']])
    stable_profiles_200_400 = len(agreements200_400[agreements200_400['direction_200'] == agreements200_400['direction_400']])
    stable_profiles_400_800 = len(agreements400_800[agreements400_800['direction_400'] == agreements400_800['direction_800']])
    return [total_profiles, stable_profiles_100_200, stable_profiles_200_400, stable_profiles_400_800]

total_number_agents = 20
count_profiles = []
count_stable_100_200 = []
count_non_stable_100_200 = []
count_stable_200_400 = []
count_non_stable_200_400 = []
count_stable_400_800 = []
count_non_stable_400_800 = []
for i in range(2, total_number_agents + 1):
    (total_profiles, stable_profiles_100_200, stable_profiles_200_400, stable_profiles_400_800) = agreements_statistics(i)
    count_profiles += [total_profiles]
    
    count_stable_100_200 += [stable_profiles_100_200]
    count_non_stable_100_200 += [total_profiles - stable_profiles_100_200]
    
    count_stable_200_400 += [stable_profiles_200_400]
    count_non_stable_200_400 += [total_profiles - stable_profiles_200_400]
    
    count_stable_400_800 += [stable_profiles_400_800]
    count_non_stable_400_800 += [total_profiles - stable_profiles_400_800]

total_non_stable_100_200 = sum(count_non_stable_100_200)
total_non_stable_200_400 = sum(count_non_stable_200_400)
total_non_stable_400_800 = sum(count_non_stable_400_800)

plt.bar([0, 1, 2], [total_non_stable_100_200, total_non_stable_200_400, total_non_stable_400_800])
plt.xticks([0, 1, 2],[r'(100, 200)','(200, 400)', '(400, 800)'])
plt.title('Number of pairs of neighboring profiles in disagreement \n for all possible profiles between 2 and '+str(total_number_agents) + ' players \n playing WE or WF strategies')
plt.xlabel(r'Sample Deviation Graphs $\tilde{D}_n^2(n_1)$ and $\tilde{D}_n^2(n_2)$ with $(n_1, n_2)$'+ ' samples')