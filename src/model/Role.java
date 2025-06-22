package model;

public enum Role {
    CLIENTE(0),
    SEGRETERIA(1);

    private final int value;

    Role(int value) {
        this.value = value;
    }

    public static Role fromInt(int value) {
        for (Role role: values())
            if (role.getValue() == value)
                return role;
        return null;
    }

    public int getValue() {
        return value;
    }
}
