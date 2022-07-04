package superhelo.thefallenskyland.utils;

import net.minecraftforge.fluids.FluidStack;

public final class FluidStackProxy {

    private final FluidStack fluid;

    public FluidStackProxy(FluidStack fluid) {
        this.fluid = fluid;
    }

    public FluidStack getFluid() {
        return fluid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FluidStackProxy)) {
            return false;
        }

        FluidStackProxy that = (FluidStackProxy) o;

        return that.fluid.containsFluid(fluid);
    }

}
