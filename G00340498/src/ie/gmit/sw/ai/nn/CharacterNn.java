package ie.gmit.sw.ai.nn;

import ie.gmit.sw.ai.nn.activator.*;

/*
 * CharacterNn  class, and is used to training data, sets expected output and actions for neural network..
 */

public class CharacterNn {

	private static NeuralNetwork nn;

	private static double[][] data = { // Health, Sword, Enemies
			{ 2, 0, 0 }, { 2, 1, 0 }, { 2, 0, 1 }, { 2, 1, 1 }, { 2, 1, 2 }, { 2, 0, 2 }, { 1, 0, 0 }, { 1, 1, 0 },
			{ 1, 0, 1 }, { 1, 1, 1 }, { 1, 1, 2 }, { 1, 0, 2 }, { 0, 0, 0 }, { 0, 1, 0 }, { 0, 0, 1 }, { 0, 1, 1 },
			{ 0, 1, 2 }, { 0, 0, 2 } };

	private static double[][] expected = { // Attack, Run
			{ 1.0, 0.0 }, { 1.0, 0.0 }, { 1.0, 0.0 }, { 0.0, 1.0 }, { 0.0, 1.0 }, { 0.0, 1.0 }, { 1.0, 0.0 },
			{ 1.0, 0.0 }, { 1.0, 0.0 }, { 1.0, 0.0 }, { 0.0, 1.0 }, { 1.0, 0.0 }, { 1.0, 0.0 }, { 1.0, 0.0 },
			{ 1.0, 0.0 }, { 1.0, 0.0 }, { 1.0, 0.0 }, { 1.0, 0.0 } };

	public void train() {
		nn = new NeuralNetwork(Activator.ActivationFunction.Sigmoid, 3, 2, 2);
		Trainator trainer = new BackpropagationTrainer(nn);
		trainer.train(data, expected, 0.02, 10000);
	}

	public CharacterNn() {

	}

	public int action(double health, double sword, double enemies) {
		double[] params = { health, sword, enemies };
		double[] result = null;
		try {
			result = nn.process(params);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (double val : result) {
			System.out.println(val);
		}
		int choice = Utils.getMaxIndex(result) + 1;

		return choice;
	}

}