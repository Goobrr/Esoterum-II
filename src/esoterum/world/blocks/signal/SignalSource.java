package esoterum.world.blocks.signal;

public class SignalSource extends SignalBlock{
    public SignalSource(String name){
        super(name);

        hasGraph = false;
        canFloodfill = false;
    }

    public class SignalSourceBuild extends SignalBuild{
        public boolean signal = false;

        @Override
        public void update(){
            super.update();

            signal = getSignal();

            propagate();
        }

        public boolean getSignal(){
            return signal;
        }

        public void propagate(){
            for(int i : outputs()){
                sendSignal(i, signal);
            }
        }

        @Override
        public boolean signalAtOutput(int index){
            for(int i : outputs()){
                if(i == index) return signal;
            }
            return false;
        }


    }
}
