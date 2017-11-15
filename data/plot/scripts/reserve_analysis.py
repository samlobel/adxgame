#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Wed Nov  8 15:04:09 2017

@author: eareyanv
"""
import deviation_analysis
import pandas as pd
import setup
import sys
import stats_mean_best_response_graphs
import networkx as nx
import matplotlib.pyplot as plt

def save_reserve_agreement_data(n1, n2, number_of_agents, reserve):
    list_of_dataframes = []
    print('Computing agreement data for ', number_of_agents, ' agents, reserve ',reserve ,', between ', n1, ' and ', n2 ,' samples')
    for supply, demand in setup.get_grid_supply_demand():
        print('\t(supply, demand) = (', supply, ',', demand, ')')
        list_of_dataframes += [deviation_analysis.get_direction_dataframe('-' + str(number_of_agents) + '-agents-vary-reserve', n1, n2, number_of_agents, supply, demand, reserve)]
    final_dataframe = pd.concat(list_of_dataframes)
    final_dataframe.to_csv(setup.create_dir(setup.path_to_data + 'stability-reserve/' + str(n1) + '-' + str(n2) + '/') + 'stability-for-' + str(number_of_agents) + '-agents-reserve-' + str(reserve) + '.csv', index = False)
    
command_line_arguments = sys.argv
if len(command_line_arguments) > 1:
    reserve_price = command_line_arguments[1]
    number_of_agents = 20
    print('Call from command line -> reserve_price = ' + str(reserve_price))
    #save_reserve_agreement_data(100, 200, number_of_agents, int(reserve_price))
    stats_mean_best_response_graphs.save_sink_eq_stats('-' + str(number_of_agents) + '-agents-vary-reserve', 200, number_of_agents, reserve_price)
    
def get_worst_sink_equilibria_profile(number_of_games, number_of_agents, supply, demand, reserve):
    """
    Given supply, demand, and reserve, outputs the profile with lowest
    revenue among all profiles of all sink equilibria.
    """
    data = pd.read_csv(setup.path_to_data + 'sinkequilibria-reserve/' + str(number_of_games) + '/sink-equilibria-for-' + str(number_of_agents) + '-agents-reserve-' + str(reserve) + '.csv')
    data = data[(data['impressions'] == supply) & (data['demand_factor'] == demand)]    
    worst_sink_equilibria_value = float("inf")
    worst_sink_equilibria = None
    grouped = data.groupby('sink_index')
    for name, group in grouped:
        worst_profile = group.loc[group['revenue_mean'].idxmin()]
        if(worst_profile['revenue_mean'] < worst_sink_equilibria_value):
            worst_sink_equilibria = worst_profile
            worst_sink_equilibria_value = worst_profile['revenue_mean']
    if(worst_sink_equilibria is not None):
        return worst_sink_equilibria
    else:
        raise ValueError('Could not find a sink equilibria!!!')
      
def save_worst_eq(number_of_games, number_of_agents):
    worst_reserve = []
    for supply, demand in setup.get_grid_supply_demand():
        #for i in range(0,131):
        for i in [0, 15, 25, 50, 75, 80]:
            x = get_worst_sink_equilibria_profile(number_of_games, number_of_agents, supply, float(demand), i)
            worst_reserve.append((i, supply, demand, x['WE'], x['WF'], x['revenue_mean']))
    data = pd.DataFrame(worst_reserve)
    data.columns = ['reserve','impressions','demand_factor','WE','WF','revenue_mean']
    data[['reserve','WE','WF']] = data[['reserve','WE','WF']].astype(int)
    data.to_csv(setup.create_dir(setup.path_to_data + 'worstequilibria-reserve/' + str(number_of_games)) + '/worst-equilibria-for-' + str(number_of_agents) + '-agents-all-reserves.csv', index = False)

"""number_of_games = 800
number_of_agents = 8
demand = 1.25
data = pd.read_csv("../../worstequilibria-reserve/" + str(number_of_games) + "/worst-equilibria-for-" + str(number_of_agents) + "-agents-all-reserves-demand-" + str(demand) + "-OPT.csv")
DG = nx.DiGraph()
labels = {}
reserve_nodes = [0, 15, 25, 50]
for r in reserve_nodes:
    eq_data = data[data['reserve'] == r]
    if(len(eq_data) != 1):
        raise ValueError('something went wrong!')
    eq_data = eq_data.iloc[0]
    print(eq_data['WE'], eq_data['WF'], eq_data['opt_reserve'])
    numberWE = int(eq_data['WE'])
    numberWF = int(eq_data['WF'])
    DG.add_node((numberWE, numberWF))
    labels[(numberWE, numberWF)] = r'$WE^{' + str(numberWE) + '}WF^{ ' + str(numberWF) + '}$'

pos=nx.circular_layout(DG)
#number_of_profiles = DG.number_of_nodes()
fig = plt.figure(3, figsize=(8,8))
for node in DG.nodes():
    nx.draw_networkx_nodes(DG, pos, nodelist=[node], alpha=1.0, node_color = 'blue', node_size = 5000)
#nx.draw_networkx_edge_labels(DG, pos, edge_labels=edge_labels, label_pos = 0.3)
nx.draw_networkx_edges(DG, pos)
nx.draw_networkx_labels(DG, pos, labels, font_size=16, font_color='red')
plt.axis('off')
#nx.draw_networkx_labels(DG, pos, labels, font_size=16, font_color='red')"""



def get_reserve_eq_table(number_of_games, number_of_agents, demand):

    data = pd.read_csv("../../worstequilibria-reserve/" + str(number_of_games) + "/worst-equilibria-for-" + str(number_of_agents) + "-agents-all-reserves-demand-" + str(demand) + "-OPT.csv")
    reserve_nodes = [0, 15, 25, 50, 75, 80]
    
    table = """
    \\begin{subtable}{.4\linewidth}
      \\centering
        \\caption{$\delta = """ + str(demand) + """$}
            \\begin{tabular}{|c|c|c|c|} \hline
                $r$ 	\t& $\StratProfile^*_r$ 	\t& $OPT(\StratProfile^*_r)$ \t& Revenue  \\\\\\hline\n"""
    
    for r in reserve_nodes:
        eq_data = data[data['reserve'] == r]
        if(len(eq_data) != 1):
            raise ValueError('something went wrong!')
        eq_data = eq_data.iloc[0]
        if("{0:.2f}".format(eq_data['opt_reserve']) == "{0:.2f}".format(r / 100.0)):
            table += """\\rowcolor{RowColor}"""
        table += '\t\t' + "{0:.2f}".format(r / 100.0) + '\t&' +  str(int(eq_data['WE'])) + ', ' +  str(int(eq_data['WF'])) + '\t&' +  "{0:.2f}".format(eq_data['opt_reserve']) + '\t&' +  "{0:.2f}".format(eq_data['revenue_mean']) +  '\\\\ \n'
    
    table += """
          \\hline
        \\end{tabular}
        \\label{tab:Equilibria""" + str(demand) + """}
    \\end{subtable}"""
    
    return table

def get_reserve_eq_table_summary(number_of_games, number_of_agents):
    table = """
\\begin{table}[!htb]
    \\caption{Equilibria dynamics """ + str(number_of_agents) + """ agents}"""
    table += get_reserve_eq_table(number_of_games, number_of_agents, 0.25)
    #table += get_reserve_eq_table(number_of_games, number_of_agents, 0.5)
    #table += get_reserve_eq_table(number_of_games, number_of_agents, 0.75)
    #table += get_reserve_eq_table(number_of_games, number_of_agents, 1.0)
    #table += get_reserve_eq_table(number_of_games, number_of_agents, 1.25)
    #table += get_reserve_eq_table(number_of_games, number_of_agents, 1.5)
    table += get_reserve_eq_table(number_of_games, number_of_agents, 1.75)
    #table += get_reserve_eq_table(number_of_games, number_of_agents, 2.0)
    table += get_reserve_eq_table(number_of_games, number_of_agents, 2.25)
    #table += get_reserve_eq_table(number_of_games, number_of_agents, 2.5)
    table += get_reserve_eq_table(number_of_games, number_of_agents, 2.75)
    #table += get_reserve_eq_table(number_of_games, number_of_agents, 3.0)
    table += """\n\\label{tab:Equilibria""" + str(number_of_agents) + """Agents}"""
    table += """\n\\end{table}"""
    print(table)
    
def get_one_latex_row(number_of_games, number_of_agents, demand):
    map_demand_opt_reserve = {0.25: 80, 1.75: 80, 2.25: 80, 2.75: 80}
    # This next map for 8 agents.
    #map_demand_opt_reserve = {0.25: 80, 1.75: 83, 2.25: 81, 2.75: 81}
    data = pd.read_csv("../../worstequilibria-reserve/" + str(number_of_games) + "/worst-equilibria-for-" + str(number_of_agents) + "-agents-all-reserves-demand-" + str(demand) + "-OPT.csv")
    reserve_0 = data[data['reserve'] == 0]
    reserve_0 = reserve_0.iloc[0]
    opt_rev = get_optimal_revenue(number_of_games, number_of_agents, demand, map_demand_opt_reserve[demand])
    
    return str(demand)  + """ & """ + str(int(reserve_0['WE'])) + """, """ + str(int(reserve_0['WF'])) + """ & """ + "{0:.2f}".format(reserve_0['revenue_mean']) + """ & """ + "{0:.2f}".format(reserve_0['opt_reserve']) + ' &' + str(int(opt_rev[0])) + ', ' + str(int(opt_rev[1])) + ' & ' + "{0:.2f}".format(opt_rev[2]) + ' & ' + "{0:.2f}".format(opt_rev[2]-reserve_0['revenue_mean'])  +  '\\\\\n'

def get_optimal_revenue(number_of_games, number_of_agents, demand, reserve):
    data = pd.read_csv("../../worstequilibria-reserve/" + str(number_of_games) + "/worst-equilibria-for-" + str(number_of_agents) + "-agents-all-reserves-demand-" + str(demand) + "-OPT.csv")    
    reserve_data = data[data['reserve'] == reserve]
    reserve_data = reserve_data.iloc[0]
    return (reserve_data['WE'],reserve_data['WF'],reserve_data['revenue_mean'])

demand_list = [0.25, 1.75, 2.25, 2.75]
latex_table = ''
for demand in demand_list:
    #latex_table += get_one_latex_row(800, 8, demand)
    latex_table += get_one_latex_row(200, 20, demand)

print(latex_table)