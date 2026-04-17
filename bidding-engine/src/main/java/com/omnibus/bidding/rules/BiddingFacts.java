package com.omnibus.bidding.rules;

import com.omnibus.bidding.domain.HandProfile;

public record BiddingFacts(
    String system,
    String auction,
    String hand,
    int hcp,
    int spades,
    int hearts,
    int diamonds,
    int clubs,
    boolean balanced
) {

    public static BiddingFacts from(HandProfile handProfile, String auction, String system) {
        return new BiddingFacts(
            system,
            auction == null ? "" : auction,
            handProfile.normalizedHand(),
            handProfile.hcp(),
            handProfile.spades(),
            handProfile.hearts(),
            handProfile.diamonds(),
            handProfile.clubs(),
            handProfile.balanced()
        );
    }
}
