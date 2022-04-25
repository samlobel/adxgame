"""
Given a history of second price auction bids, m x n, 
where m is the number of auctions ran, and n is the number of bidders,
test each of the first and second highest bid found in each auction
as a reserve price.
"""
import matplotlib.pyplot as plt
import numpy as np
import pandas as pd
import setup

def test_bids_as_reserve_prices(bidHistory):
    """Find the empirically best reserve price for a second price auction.

    Input:
        bidHistory: ndarray.  history length x number of bidders
    Return:
        reservePrices: ndarray.  history length * 2
            Reserve prices tested
        totalExpectedRevenue: ndarray.  historylength * 2
            reservePrice[i] corresponds to totalExpectedRevenue[i].
    """
    bidHistory = handle_one_bidder_setting(bidHistory)
    # Get the top to highest bids per auction
    # Sort so that each row is increasing.
    bidHistory.sort()
    # After sorting, the top two bids are the last two elements
    # in each row.
    reservePrices = np.zeros(bidHistory.shape[0] * 2)
    reservePrices = np.concatenate((bidHistory[:,-2], bidHistory[:,-1]))
    # Do not process duplicates
    reservePrices = np.unique(reservePrices)
    # Process in ascending order
    reservePrices.sort()
    # Test each bid and see how they do as a reserve price
    totalExpectedRevenue = np.zeros(reservePrices.shape[0])
    for ii, reservePrice in enumerate(reservePrices):
        prices = np.maximum(reservePrice, bidHistory[:,-2])
        highestBidWon = bidHistory[:,-1] >= prices
        payment = highestBidWon * prices
        totalExpectedRevenue[ii] = np.mean(payment)
    
    return reservePrices, totalExpectedRevenue


def handle_one_bidder_setting(bidHistory):
    """Add an additional column if there is only one bidder.
    Input:
        bidHistory: ndarray.  history length x number of bidders
            Bidding data
    Return:
        bidHistory. ndarray.  history length x max(2, number of bidders)
            Bidding data with an additional column if the number
            of bidders is 1.
    """
    if bidHistory.shape[1] == 1:
        newBidHistory = np.zeros((bidHistory.shape[0], 2))
        newBidHistory[:,-1] = bidHistory[:,0]
        bidHistory = newBidHistory

    return bidHistory


def get_optimal_reserve_price(reservePrices, totalExpectedRevenue):
    """Get the optimal reserve price, and the total expected revenue associated with it.
    Input:
        reservePrices: ndarray.  history length * 2
            Reserve prices tested
        totalExpectedRevenue: ndarray.  historylength * 2
            reservePrice[i] corresponds to totalExpectedRevenue[i].    
    Return:
        optReservePrice: numpy.float64.
            Optimal empirical reserve price.
        maxTotalExpectedRevenue: numpy.float64.
            Expected revenue using the optimal reserve price.
    """
    maxTotalExpectedRevenue = np.max(totalExpectedRevenue)
    optReservePrice = reservePrices[np.argmax(totalExpectedRevenue)]

    return optReservePrice, maxTotalExpectedRevenue


def plot_expected_revenue(reservePrices, totalExpectedRevenue):
    """Plot expected revenue as a function of reserve price.
    Input:
        reservePrices: ndarray.  history length * 2
            Reserve prices tested
        totalExpectedRevenue: ndarray.  historylength * 2
            reservePrice[i] corresponds to totalExpectedRevenue[i].
    Return:
        None
    """
    # Annotate where the optimal is on the plot
    optReservePrice, maxTotalExpectedRevenue = get_optimal_reserve_price(
        reservePrices, totalExpectedRevenue)

    fig = plt.figure()
    ax = fig.add_subplot(111)
    plt.plot(reservePrices, totalExpectedRevenue)
    plt.plot(optReservePrice, maxTotalExpectedRevenue, 'o')
    plt.xlabel('Reserve Price')
    plt.ylabel('Total Expected Revenue')
    annotation = '(' + "{:.3f}".format(optReservePrice) + ', ' \
        + "{:.3f}".format(maxTotalExpectedRevenue) + ')'
    ax.annotate(annotation, xy=(optReservePrice  - .1, maxTotalExpectedRevenue - .1))
    plt.grid()
    plt.show()


def get_optimal_reserve(number_of_games, number_of_agents, numberWE, numberWF, supply, demand, reserve):
    # Real data auction
    file_location = "../../results/" + str(number_of_agents) + "-agents-worstequilibria-bids/" + str(number_of_games) + "/" + str(supply) + "/" + str(demand) + "/bidlogs/WEWF(" + str(numberWE) + "-" + str(numberWF) + ")-r(" + str(reserve) + ").csv"
    print(file_location)
    data = pd.read_csv(file_location, names = ['bid1','bid2','bid3','bid4', 'bid5'], header = None)
    data = data.fillna(0)
    bidHistory = data.as_matrix()    
    # Test the first and second highest bids as reserve
    reservePrices, totalExpectedRevenue = test_bids_as_reserve_prices(bidHistory)
    # Find what reserve price was optimal
    optReservePrice, maxTotalExpectedRevenue = get_optimal_reserve_price(reservePrices, totalExpectedRevenue)
    #print("Optimal reserve price:", optReservePrice)
    #print("Revenue at opptimal reserve price:", maxTotalExpectedRevenue)
    # Plot what was seen
    plot_expected_revenue(reservePrices, totalExpectedRevenue)

    return optReservePrice

number_of_games = 200
number_of_agents = 20
for demand in [0.0, 0.25, 0.5, 0.75, 1.0, 1.25, 1.5, 1.75, 2.0, 2.25, 2.5, 2.75, 3.0]:
    data = pd.read_csv("../../worstequilibria-reserve/" + str(number_of_games) + "/worst-equilibria-for-" + str(number_of_agents) + "-agents-all-reserves.csv")
    data[['reserve','WE','WF']] = data[['reserve','WE','WF']].astype(int)
    data = data[data['demand_factor'] == demand]
    for index, row in data.iterrows():
        #if(int(row['reserve']) <= 90):
        if(int(row['reserve']) in [0, 15, 25, 50, 75, 80]):
            opt_reserve = get_optimal_reserve(number_of_games, number_of_agents, int(row['WE']), int(row['WF']), int(row['impressions']), row['demand_factor'], int(row['reserve']))
            data.loc[index, 'opt_reserve']= opt_reserve
            print(opt_reserve)
    data.to_csv(setup.path_to_data + 'worstequilibria-reserve/' + str(number_of_games) + '/worst-equilibria-for-' + str(number_of_agents) + '-agents-all-reserves-demand-' + str(demand) + '-OPT.csv', index = False)




