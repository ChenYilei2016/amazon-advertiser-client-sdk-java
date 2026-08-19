package io.github.chenyilei2016.amazonads.adsv1.targets.api;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TargetsApiContractTest {

    @Test
    void shouldExposeOnlyTheFourTargetsOperations() {
        Set<String> operationNames = Arrays.stream(TargetsApi.class.getDeclaredMethods())
                .map(method -> method.getName())
                .filter(name -> Set.of("createTarget", "deleteTarget", "queryTarget", "updateTarget").contains(name))
                .collect(Collectors.toSet());

        assertEquals(Set.of("createTarget", "deleteTarget", "queryTarget", "updateTarget"), operationNames);
    }
}
