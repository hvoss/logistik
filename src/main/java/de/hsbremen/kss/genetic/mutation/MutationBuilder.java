package de.hsbremen.kss.genetic.mutation;

import java.util.ArrayList;
import java.util.List;

import de.hsbremen.kss.util.RandomUtils;

public class MutationBuilder {

    private final RandomUtils randomUtils;

    private final List<Mutation> mutations = new ArrayList<Mutation>();

    public MutationBuilder(final RandomUtils randomUtils) {
        this.randomUtils = randomUtils;
    }

    public MutationBuilder allocateRouteMutation(final int num) {
        for (int i = 0; i < num; i++) {
            this.mutations.add(new AllocateRouteMutationImpl(this.randomUtils));
        }

        return this;
    }

    public MutationBuilder moveActionMutation(final int num) {
        for (int i = 0; i < num; i++) {
            this.mutations.add(new MoveActionMutationImpl(this.randomUtils));
        }

        return this;
    }

    public MutationBuilder moveSubrouteMutation(final int num) {
        for (int i = 0; i < num; i++) {
            this.mutations.add(new MoveSubrouteMutation(this.randomUtils));
        }

        return this;
    }

    public MutationBuilder swapOrderMutation(final int num) {
        for (int i = 0; i < num; i++) {
            this.mutations.add(new SwapOrderMutationImpl(this.randomUtils));
        }

        return this;
    }

    public MutationBuilder nullMutation(final int num) {
        for (int i = 0; i < num; i++) {
            this.mutations.add(null);
        }

        return this;
    }

    public MutationBuilder combineTwoToursMutation(final int num) {
        for (int i = 0; i < num; i++) {
            this.mutations.add(new CombineTwoToursMutationImpl(this.randomUtils));
        }

        return this;
    }

    public MutationBuilder splitTourMutation(final int num) {
        for (int i = 0; i < num; i++) {
            this.mutations.add(new SplitTourMutation(this.randomUtils));
        }

        return this;
    }

    public List<Mutation> build() {
        return this.mutations;
    }

}
