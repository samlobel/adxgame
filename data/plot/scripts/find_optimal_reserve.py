"""
Given a history of second price auction bids, m x n, 
where m is the number of auctions ran, and n is the number of bidders,
test each of the first and second highest bid found in each auction
as a reserve price.
"""
import matplotlib.pyplot as plt
import numpy as np
import pandas as pd
import time


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
    print(type(maxTotalExpectedRevenue))

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


if __name__ == "__main__":
    # Example: Uniform distribution
    # Sampled auction data
    #bidHistoryLen = 10000
    #nBidder = 4
    #bidHistory = np.random.rand(bidHistoryLen, nBidder)
    #bidHistory = np.random.randint(0, 11, (bidHistoryLen, nBidder))
    #bidHistory = handle_one_bidder_setting(bidHistory)


    # Real data auction
    numberWE = 0
    numberWF = 8
    demand = 3.0
    number_of_games = 200
    reserve = 0.8361
    data = pd.read_csv("../../results/small-tests/" + str(number_of_games) + "/2000/" + str(demand) + "/bidlogs/WEWF(" + str(numberWE) + "-" + str(numberWF) + ")-r(" + str(reserve) + ").csv", names = ['bid1','bid2','bid3','bid4', 'bid5'], header = None)
    data = data.fillna(0)
    bidHistory = data.as_matrix()
    nBidder = len(bidHistory[0])


    # Test the first and second highest bids as reserve
    startTime = time.time()
    reservePrices, totalExpectedRevenue = test_bids_as_reserve_prices(bidHistory)
    endTime = time.time()
    elapsedTime = endTime - startTime
    print("Testing bids, elapsed time:", elapsedTime)

    # Find what reserve price was optimal
    optReservePrice, maxTotalExpectedRevenue = get_optimal_reserve_price(
        reservePrices, totalExpectedRevenue)
    print("Optimal reserve price:", optReservePrice)
    print("Revenue at opptimal reserve price:", maxTotalExpectedRevenue)
    
    # Plot what was seen
    plot_expected_revenue(reservePrices, totalExpectedRevenue)


