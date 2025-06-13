package com.example.cheesePlatterAgent.data;

public enum CheeseType {
    HARD("Hard and semi-hard cheese"),
    WHITE_MOLD("White mold cheese"),
    RED_WHITE_RIND("Red and white rind cheese"),
    GOAT_SHEEP("Goat and sheep cheese"),
    BLUE("Blue cheese");

    public final String description;

    private CheeseType(String description) {
        this.description = description;
    }

    public static CheeseType valueOfDescription(String description) {
        for (CheeseType e : values()) {
            if (e.description.equals(description)) {
                return e;
            }
        }
        return null;
    }

    public String getDescription() {
        return description;
    }
}
