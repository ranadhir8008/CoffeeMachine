package system;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Outlets {

    @JsonProperty("count_n")
    private int count;

    public int getCount() {
        return count;
    }

}
