package adx.structures;

public class CampaignSpendingLimit {
	private Campaign campaign;
	private double limit;
	
	public CampaignSpendingLimit(Campaign campaign, double limit) {
		this.campaign = campaign;
		this.limit = limit;
	}

	public Campaign getCampaign() {
		return campaign;
	}

	public double getLimit() {
		return limit;
	}
}
