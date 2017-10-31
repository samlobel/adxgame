#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Fri Oct 27 13:20:03 2017

@author: eareyanv
"""
import pandas as pd
import matplotlib.pyplot as plt

def stability_statistics(number_of_agents):
    stability1 = pd.read_csv('../../stability/100-200/stability-for-' + str(number_of_agents) + '-agents.csv')
    stability2 = pd.read_csv('../../stability/200-400/stability-for-' + str(number_of_agents) + '-agents.csv')
    stability3 = pd.read_csv('../../stability/400-800/stability-for-' + str(number_of_agents) + '-agents.csv')
    
    total_profiles = len(stability1)
    if(total_profiles != len(stability2) or total_profiles != len(stability3)):
        raise ValueError('The length of the stability data do not agree')
    stable_profiles_100_200 = len(stability1[stability1['100-200-agreement'] == 1])
    stable_profiles_200_400 = len(stability2[stability2['200-400-agreement'] == 1])
    stable_profiles_400_800 = len(stability3[stability3['400-800-agreement'] == 1])
    
    #print('Number of agents = ' , number_of_agents)
    #print('100-200 -> ' + str(total_profiles) + ', stable = ' +  str(stable_profiles_100_200))
    #print('200-400 -> ' + str(total_profiles) + ', stable = ' +  str(stable_profiles_200_400))
    #print('Final non stable -> ' + str(total_profiles - stable_profiles_200_400))
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
    (total_profiles, stable_profiles_100_200, stable_profiles_200_400, stable_profiles_400_800) = stability_statistics(i)
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
plt.xticks([0, 1, 2],['(100, 200)','(200, 400)', '(400, 800)'])
plt.title('Number of profiles in disagreement \n for all possible profiles between 2 and '+str(total_number_agents) + ' players \n playing WE or WF strategies')
plt.xlabel(r'$(n_1, n_2)$'+ ' samples')