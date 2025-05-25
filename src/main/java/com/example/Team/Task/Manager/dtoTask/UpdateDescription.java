package com.example.Team.Task.Manager.dtoTask;

import lombok.Data;
import lombok.NonNull;
import org.antlr.v4.runtime.misc.NotNull;

@Data
public class UpdateDescription {
    @NotNull
    private String newDescription;
}
