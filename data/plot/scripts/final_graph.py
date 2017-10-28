#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Fri Oct 27 15:59:00 2017

@author: eareyanv
"""
import pandas as pd
import networkx as nx
import deviation_analysis


def compute_edges(profile1_data, profile2_data, profile3_data, agreement1, agreement2, agreement3):
    """
    Given three adjacent profiles WE^{p+1}WF^{n-p-1}, WE^{p}WF^{n-p}, WE^{p-1}WF^{n-p+1},
    and the agreements for each profile,
    compute the outgoing edges for the middle profile: WE^{p}WF^{n-p}
    """
    print(profile1_data)
    print(profile2_data)
    print(profile3_data)

    profile1_name = str(profile1_data[0])
    profile2_name = str(profile2_data[0])
    profile3_name = str(profile3_data[0])
    print('profile1_name = ', profile1_name)
    print('profile2_name = ', profile2_name)
    print('profile3_name = ', profile3_name)
    
    # First, check that the profiles are indeed adjacent.
    if(     (profile1_name.count('WE') - 1 == profile2_name.count('WE')) 
        and (profile2_name.count('WE') - 1 == profile3_name.count('WE'))
        and (profile1_name.count('WF') + 1 == profile2_name.count('WF'))
        and (profile2_name.count('WF') + 1 == profile3_name.count('WF'))
        ):
        # Ok to continue, the profiles are adjacent.
        # Check the agreements. If the profiles agree, add a weight of one, 
        # otherwise compute the probabilitiy of deviation of the sample mean.
        print('OK')
    else:
        raise ValueError("The 3 given profiles are not adjacent --> \\n profile1_data = " , profile1_data, ", \\n profile2_data = ", profile2_data , "\\n profile3_data = ", profile3_data)
    # Next, 
    pass


number_of_agents = 4
n1 = 200
n2 = 400
impressions = '2000'
demand_factor = 0.25

stability = pd.read_csv('../../stability/' + str(n1) + '-' + str(n2) + '/stability-for-' + str(number_of_agents) + '-agents.csv')
# Cascade profile data
cascade_profile = deviation_analysis.compute_cascade_profile_data(n2, number_of_agents, impressions, str(demand_factor))
number_of_profiles = len(cascade_profile)
DG = nx.DiGraph()
DG.add_nodes_from([v[0] for v in cascade_profile])
# Agreement Data
agreements = stability[stability['demand_factor'] == demand_factor]
# Take care of head (all WEs) and tail (all WFs) first

# Take care of all profiles in between head (all WEs) and tail (all WFs)
cascade_profile[0][1]['WE']
for index, row in agreements.iterrows():
    print(index, row[str(n1) + '-' + str(n2) + '-agreement'])
    """
    At this point I should have all that I need to compute the final graph.
    I have: the agreements of profiles and the sample data. 
    If there is an agreement for profile s, 
        the graph should have edges of weight one for both backward and forward
        deviation for that profile.
    Otherwise, compute the probability of deviation as given by the distribution
    of the sampling mean.
    """