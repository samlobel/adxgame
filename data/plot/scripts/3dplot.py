#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Wed Sep 27 10:12:18 2017

@author: eareyanv
"""
import matplotlib.pyplot as plt
from matplotlib import cm
from matplotlib.ticker import LinearLocator, FormatStrFormatter
import numpy as np
from plots import get_data
# Make data.
X = np.arange(1, 21, 1)
Y = np.arange(1, 21, 1)
Z = np.arange(1, 21, 1)
X, Y = np.meshgrid(X, Y)
dummy, Z = np.meshgrid(Z, np.arange(1,21,1))
# Choose agent
agent1 = 'SI'
agent2 = 'WE'

agent = 'WE'
opponent = 'SI'

column = 'profit'
for i in range(0,20):
    for j in range(0,20):
        data = get_data('../results_Version2/', agent1 + agent2 + '('+str(i+1)+'-'+str(j+1)+').csv')
        #Z[i][j] = data[data['agent'].str.contains(agent+'SIAgent')].profit.mean()
        #Z[i][j] = data[data['agent'].str.contains(agent+'SIAgent')].activate.mean()
        Z[i][j] = data[data['agent'].str.contains(agent+'Agent')][column].mean()
fig = plt.figure()
ax = fig.gca(projection='3d')
# Plot the surface.
surf = ax.plot_surface(X, Y, Z, cmap=cm.coolwarm,
                       linewidth=0, antialiased=False)
# Customize the z axis.
ax.set_xlim(0,20)
ax.set_ylim(0,20)
#ax.set_zlim(-1.01, 1.01)
ax.zaxis.set_major_locator(LinearLocator(10))
ax.zaxis.set_major_formatter(FormatStrFormatter('%.02f'))
ax.xaxis.set_major_formatter(FormatStrFormatter('%.00f'))
ax.yaxis.set_major_formatter(FormatStrFormatter('%.00f'))
# Add a color bar which maps values to colors.
fig.colorbar(surf, shrink=0.5, aspect=5)
plt.title(column + ' for agent ' + agent + ' v ' + opponent)

plt.show()