package Validator;

import Model.DataGraph;

public class main {

	public static void main(String[] args) {
		if (args.length < 1) {
            System.out.println("A filename is expected as argument! ");
            return;
        }
		
		DataGraph dataGraph = new DataGraph(args[0]);
		System.out.println(dataGraph.toString());
	}
}