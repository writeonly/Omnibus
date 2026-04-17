package com.omnibus.bidding.domain;

import java.util.Arrays;
import org.springframework.stereotype.Component;

@Component
public class HandParser {

    public HandProfile parse(String rawHand) {
        String normalized = rawHand == null ? "" : rawHand.trim().toUpperCase();
        String[] suits = normalized.split("\\s+");

        if (suits.length != 4) {
            throw new IllegalArgumentException("Hand must contain four suit groups separated by spaces");
        }

        int spades = suits[0].length();
        int hearts = suits[1].length();
        int diamonds = suits[2].length();
        int clubs = suits[3].length();
        int cardCount = Arrays.stream(suits).mapToInt(String::length).sum();

        if (cardCount != 13) {
            throw new IllegalArgumentException("Hand must contain exactly 13 cards");
        }

        int hcp = normalized.chars()
            .map(Character::toUpperCase)
            .map(this::hcpFor)
            .sum();

        boolean balanced = isBalanced(spades, hearts, diamonds, clubs);
        return new HandProfile(normalized, spades, hearts, diamonds, clubs, hcp, balanced);
    }

    private int hcpFor(int rank) {
        return switch ((char) rank) {
            case 'A' -> 4;
            case 'K' -> 3;
            case 'Q' -> 2;
            case 'J' -> 1;
            default -> 0;
        };
    }

    private boolean isBalanced(int spades, int hearts, int diamonds, int clubs) {
        int[] distribution = {spades, hearts, diamonds, clubs};
        int doubletons = 0;

        for (int suitLength : distribution) {
            if (suitLength < 2 || suitLength > 5) {
                return false;
            }
            if (suitLength == 2) {
                doubletons++;
            }
        }

        return doubletons <= 1;
    }
}
