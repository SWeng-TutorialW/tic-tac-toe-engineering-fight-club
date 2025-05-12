package il.cshaifasweng.OCSFMediatorExample.entities;

import java.io.Serializable;

public class RoleAssignment implements Serializable {
    private String symbol;  // "X" or "O"

    public RoleAssignment(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }

    @Override
    public String toString() {
        return "RoleAssignment{" +
                "symbol='" + symbol + '\'' +
                '}';
    }
}
