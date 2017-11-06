#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Tue Oct 10 08:50:01 2017

@author: eareyanv
"""
import setup
import get_data
import networkx as nx
from progress import update_progress

def get_specific_profile_data(numberWE, numberWF, specific_number_of_games, supply, demand):
    """
    Given numberWE, numberWF, specific_number_of_games, supply, demand;
    produces the profile data for numberWE playing numberWF specific_number_of_games
    times with supply and demand.
    """
    dir_location = setup.get_agent_dir_location(specific_number_of_games, supply, demand)
    file_location = 'WEWF(' + str(numberWE) + '-' + str(numberWF) + ').csv'
    mix = get_data.get_agent_data(specific_number_of_games, dir_location, file_location)
    WEdata = mix[mix.agent.str.contains('WEAgent')]
    WFdata = mix[mix.agent.str.contains('WFAgent')]
    return ('WE' * numberWE + 'WF' * numberWF, {'WE': {'n' : len(WEdata), 'mean' : WEdata.profit.mean(), 'var' : WEdata.profit.var()} , 
                                                'WF': {'n' : len(WFdata), 'mean' : WFdata.profit.mean(), 'var' : WFdata.profit.var()}})

def produce_specific_profile_data(map_profile_to_numberofgames, number_of_agents, supply, demand, reserve = 0, include_reserve = False):
    """
    Given a map: 
        {Profile:number of games}, 
    (e.g. {'WEWE': 200,'WEWF': 200,'WFWF': 400})
    produces a list of tuples:
        (Strategy profile, {WE : {n, mean profit WE, std profit WE}, WF : {n, mean profit WF, std profit WF})
    where the number of samples of each profile is given by the map_profile_to_numberofgames.
        An strategy profile indicates the strategy played by each
    player in the game, e.g., a profile WEWEWF indicates a 
    game where two players play WE and one player plays WF.
    Note that this game is symmetric and thus, WEWEWF = WEWFWE= WFWEWE.
    For simplicity, we normalize by first indicating WEs and then WFs.
    """
    #print(map_profile_to_numberofgames)
    # Check that the map received is correct.
    # Correcteness means that the map contains all profiles.
    if(number_of_agents + 1 != len(map_profile_to_numberofgames)):
        raise ValueError('The number of agents and length of map_profile_to_numberofgames must coincide!')
    profile_data = []
    for i in range(0, number_of_agents + 1):
        profile = ('WE' * (number_of_agents - i) + 'WF' * i)
        if(profile not in map_profile_to_numberofgames):
            raise ValueError('The map does not contain profile: ' + ('WE' * (number_of_agents - i) + 'WF' * i) )
        specific_number_of_games = map_profile_to_numberofgames[profile]
        profile_data.append(get_specific_profile_data(number_of_agents-i,i,specific_number_of_games,supply,demand))
    return profile_data

def produce_profile_data(number_of_games, number_of_agents, supply, demand, reserve = 0, include_reserve = False):
    """
    Given the number of games and agents, produces a list of tuples:
        (Strategy profile, {WE : {n, mean profit WE, std profit WE}, WF : {n, mean profit WF, std profit WF})
    BY calling produce_specific_profile_data with the right input.
    """
    return produce_specific_profile_data(dict(('WE' * (number_of_agents - i) + 'WF' * i, number_of_games) for i in range(0,number_of_agents + 1)), number_of_agents, supply, demand, reserve, include_reserve)

def produce_mean_best_response_graph(profile_data):
    """
    Given the data of the graph, produces a directed graph, DG,
    which is an object of the library networkx.
    """
    number_of_profiles = len(profile_data)
    DG = nx.DiGraph()
    DG.add_nodes_from([v[0] for v in profile_data])
    for i in range(1, number_of_profiles):
        DG.add_node(profile_data[i][0])
        # WE deviating to WF, i.e, is a WE making less or equal than a WF?
        #print(profile_data[i-1][1]['WE']['mean'], profile_data[i][1]['WF']['mean'])
        if profile_data[i-1][1]['WE']['mean'] <= profile_data[i][1]['WF']['mean']:
            DG.add_edge(profile_data[i-1][0], profile_data[i][0])
        # WF deviating to WE, i.e, is a WF making less or equal than a WE?
        if profile_data[i][1]['WF']['mean'] <= profile_data[i-1][1]['WE']['mean']:
            DG.add_edge(profile_data[i][0], profile_data[i-1][0])
    return DG

def get_pure_nash(DG):
    """Get all the pure Nash reduces to getting all the
    'sink' nodes (actually, leaf nodes, since this special
    graph is just a tree). This is a list of tuples (#WE,#WF)"""
    return [(i.count('WE'), i.count('WF')) for i in DG.nodes_iter() if DG.out_degree(i)==0]

def get_dict_of_pure_nash(number_of_games, demand_factor, impressions, dir_location):
    """
    This function calls the function that plots the best response graph 
    for games with 2 to 20 players. It returns a dict of pure nash.
    """
    dict_of_pure_nash = {}
    for i in range(2, 21):
        update_progress(i/20)
        profile_data = produce_profile_data(number_of_games, dir_location, i)
        DG = produce_mean_best_response_graph(profile_data)
        dict_of_pure_nash[i] = get_pure_nash(DG)
    return dict_of_pure_nash