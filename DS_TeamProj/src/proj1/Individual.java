package proj1;

import java.util.*;

public class Individual implements java.io.Serializable {
	private int nrow;
	private int ncol;

	private int count = 0;// itemlayout�� ������ ������ item(��, ��ġ�� ������ ������ item)�� �� �� �ִ��� ����
							// �뵵(itemExist[count]�� ����)
	float[][] data; // �������� Ȯ���ϱ� ���� 225*225 ������
	float[][] dataCenter; // �߰�([7][7])�� ���� ���� ������ �� ����� �� �ʿ��� ������

	// Cache
	public Individual(ValueTable vt) {

		this.data = vt.getTable();

		this.dataCenter = vt.getTable();
		this.nrow = vt.nrow;
		this.ncol = vt.ncol;

	}

	private int[] itemExist = new int[15 * 15]; // item �߿� ������ ���� �� ���� ����
	private int[][] itemlayout = new int[15][15]; // ���� item���� ��ġ�� �� ���� ����

	/* Getters and setters */
	// Use this if you want to create individuals with different gene lengths

	// �����ϱ� ���� �޼ҵ�
	public void run() {

		// data2�� data �� ����
		float[][] data2 = new float[15 * 15][15 * 15];
		for (int i = 0; i < 15 * 15; i++) {
			for (int j = 0; j < 15 * 15; j++) {
				data2[i][j] = data[i][j];
			}
		}

		// center()�� �̿��Ͽ� [7][7]�� �� ���ϰ� ��� ��ġ�ϰ� �ִ� 3*3�� itemlayout�� �ϴ� ������
		int center = center();
		int numberOfTimes = 3;
		itemlayout[(this.nrow) / 2][(this.ncol) / 2] = center;
		// ��� 14

		itemlayout[(this.nrow) / 2 - 1][(this.ncol) / 2] = this.maxOne(center);
		itemlayout[(this.nrow) / 2][(this.ncol) / 2 + 1] = this.maxOne(center);
		itemlayout[(this.nrow) / 2 + 1][(this.ncol) / 2] = this.maxOne(center);
		itemlayout[(this.nrow) / 2][(this.ncol) / 2 - 1] = this.maxOne(center);
		// ���ڰ� 	41
		// 	55	14	60
		// 		213

		itemlayout[(this.nrow) / 2 - 1][(this.ncol) / 2 - 1] = this.maxTwo(
				itemlayout[(this.nrow) / 2][(this.ncol) / 2 - 1], itemlayout[(this.nrow) / 2 - 1][(this.ncol) / 2]);
		itemlayout[(this.nrow) / 2 - 1][(this.ncol) / 2 + 1] = this.maxTwo(
				itemlayout[(this.nrow) / 2 - 1][(this.ncol) / 2], itemlayout[(this.nrow) / 2][(this.ncol) / 2 + 1]);
		itemlayout[(this.nrow) / 2 + 1][(this.ncol) / 2 + 1] = this.maxTwo(
				itemlayout[(this.nrow) / 2][(this.ncol) / 2 + 1], itemlayout[(this.nrow) / 2 + 1][(this.ncol) / 2]);
		itemlayout[(this.nrow) / 2 + 1][(this.ncol) / 2 - 1] = this.maxTwo(
				itemlayout[(this.nrow) / 2][(this.ncol) / 2 - 1], itemlayout[(this.nrow) / 2 + 1][(this.ncol) / 2]);
		// 3*3 �ϼ� 	9	41	17
		// 			55	14	60
		// 			112 213 147

		// ������ �ϼ�
		for (int i = 0; i < this.nrow / 2 - 1; i++) {
			// a,b,c,d�� maxOne(n)�� �̿��Ͽ� ���ϴ� ����
			// 		a
			// 		9	41	17	b
			// 		55	14	60
			// 	d	112	213	147
			// 				c
			itemlayout[5 - i][6 - i] = this.maxOne(itemlayout[6 - i][6 - i]);
			itemlayout[6 - i][9 + i] = this.maxOne(itemlayout[6 - i][8 + i]);
			itemlayout[9 + i][8 + i] = this.maxOne(itemlayout[8 + i][8 + i]);
			itemlayout[8 + i][5 - i] = this.maxOne(itemlayout[8 + i][6 - i]);

			for (int j = 0; j < numberOfTimes; j++) {
				// a,...,l�� maxTwo(n1, n2)�� �̿��� ���ϴ� ����
				// 	l	189	a	b	c
				// 	k	9	4	17	144
				// 	j	55	14	60	d
				// 	119	112	213	147	e
				// 	i	h	g	80	f
				itemlayout[5 - i][7 - i + j] = this.maxTwo(itemlayout[5 - i][6 - i + j], itemlayout[6 - i][7 - i + j]);
				itemlayout[7 - i + j][9 + i] = this.maxTwo(itemlayout[6 - i + j][9 + i], itemlayout[7 - i + j][8 + i]);
				itemlayout[9 + i][7 + i - j] = this.maxTwo(itemlayout[9 + i][8 + i - j], itemlayout[8 + i][7 + i - j]);
				itemlayout[7 + i - j][5 - i] = this.maxTwo(itemlayout[8 + i - j][5 - i], itemlayout[7 + i - j][6 - i]);

			}
			numberOfTimes = numberOfTimes + 2;//1ȸ ������ ������ j���� 2�� ����
		}
		/* ��ġ �ٲ��� �� ���� �󸶳� ���� �ٲ �˾ƺ���
		int x=itemlayout[1][14];
		itemlayout[1][14]=itemlayout[10][5];
		itemlayout[10][5]=x;
		int y=itemlayout[6][9];
		itemlayout[6][9]=itemlayout[11][6];
		itemlayout[11][6]=y;
		int z=itemlayout[7][7];
		itemlayout[7][7]=itemlayout[3][5];
		itemlayout[3][5]=z;
		int a=itemlayout[8][8];
		itemlayout[8][8]=itemlayout[12][12];
		itemlayout[12][12]=a;
		*/
		
		// itemlayout ���
		for (int i = 0; i < 15; i++) {
			System.out.print("[");
			for (int j = 0; j < 15; j++) {
				System.out.printf(itemlayout[i][j] + "	");
			}
			System.out.println("]");
		}

		System.out.println("Fittest value: " + this.getObjectiveValue(data2));

	}

	// �߾Ӱ� ���ϴ� �޼ҵ�
	public int center() {

		float max1 = 0, max2 = 0, max3 = 0, max4 = 0;
		int maxIndex = 0;
		float maxSum[] = new float[15 * 15];// �� �ึ���� max1, max2, max3, max4�� �� ����
		float max = 0;

		for (int i = 0; i < nrow; i++) {
			// �࿡�� ���� ū �� max1�� ����
			for (int j = 0; j < ncol; j++) {
				if (dataCenter[i][j] >= max1) {
					max1 = dataCenter[i][j];
					maxIndex = j;
				}
			}
			data[i][maxIndex] = 0;// i���� ���� ū ���� ������ ����(�������� ������ �ι�°�� ū ���� ���� �� �ٽ� �� ���� ����ǹǷ�)
			// �࿡�� �ι�°�� ū �� max2�� ����
			for (int j = 0; j < ncol; j++) {
				if (dataCenter[i][j] >= max2) {
					max2 = dataCenter[i][j];
					maxIndex = j;
				}
			}
			data[i][maxIndex] = 0;
			for (int j = 0; j < ncol; j++) {
				if (dataCenter[i][j] >= max3) {
					max3 = dataCenter[i][j];
					maxIndex = j;
				}
			}
			data[i][maxIndex] = 0;
			for (int j = 0; j < ncol; j++) {
				if (dataCenter[i][j] >= max4) {
					max4 = dataCenter[i][j];
					maxIndex = j;
				}
			}
			data[i][maxIndex] = 0;
			maxSum[i] = max1 + max2 + max3 + max4;
		}
		// maxSum[i]�� ���ؼ� ���� ū ���� maxIndex�� ����
		for (int i = 0; i < nrow; i++) {
			if (maxSum[i] >= max) {
				max = maxSum[i];
				maxIndex = i;
			}
		}
		itemExist[0] = maxIndex;// ù��°�� ��ġ�� ������ item�� itemExist[0]�� ����
		return maxIndex;
	}

	// n�� ���� �������� ���� �� ã�� �޼ҵ�
	public int maxOne(int n) {
		float max = data[n][0];
		int maxIndex = 0;
		// n�� �ش��ϴ� item�� ���� �������� ū item�� maxIndex�� ����
		for (int i = 0; i < data[n].length; i++) {
			if (data[n][i] >= max) {
				max = data[n][i];
				maxIndex = i;
			}
		}
		count++;
		itemExist[count] = maxIndex; // ��ġ�� ������ item�� itemExist�� ����(count�� �̿��ؼ� ��ġ�� ������ ������ 1�� �����ϵ��� ����)

		// ��ġ�� ������ item������ ������ data�� ��������(�׷��� �� ���� �������� ��ġ�� ������ item�� �ٽ� ���� ���� ����)
		for (int i = 0; i < count; i++) {
			data[itemExist[i]][maxIndex] = 0;
			data[maxIndex][itemExist[i]] = 0;
		}

		return maxIndex;
	}

	// n1, n2�� ���� �������� ���� �� ã��
	public int maxTwo(int n1, int n2) {

		// data���� n1��� n2���� ���Ͽ� sumOfTwo�� ����
		float[] sumOfTwo = new float[15 * 15];
		for (int i = 0; i < data[n1].length; i++) {
			sumOfTwo[i] = data[n1][i] + data[n2][i];
		}

		float max = sumOfTwo[0];
		int maxIndex = 0;
		// sumOfTwo���� ���� ū ���� �����Ǵ� item�� maxIndex�� ����
		for (int i = 0; i < data[n1].length; i++) {
			if (sumOfTwo[i] >= max) {
				max = sumOfTwo[i];
				maxIndex = i;
			}
		}

		count++;
		itemExist[count] = maxIndex; // ��ġ�� ������ item�� itemExist�� ����

		// ��ġ�� ������ item������ ������ data�� ��������(�׷��� �� ���� �������� ��ġ�� ������ item�� �ٽ� ���� ���� ����)
		for (int i = 0; i < count; i++) {
			data[itemExist[i]][maxIndex] = 0;
			data[maxIndex][itemExist[i]] = 0;
		}

		return maxIndex;
	}

	public int getValue(int row, int col) {
		return itemlayout[row][col];
	}

	public ArrayList<Integer> getGeneArray() {
		ArrayList<Integer> array = new ArrayList<Integer>();
		for (int i = 0; i < this.rsize(); i++) {
			for (int j = 0; j < this.csize(); j++) {
				array.add(this.getValue(i, j));
			}
		}
		return array;
	}

	/* Public methods */
	public int rsize() {
		return nrow;
	}

	public int csize() {
		return ncol;
	}

	public int psize() {
		return nrow * ncol;
	}

	public void setSize(int nrow, int ncol) {
		this.nrow = nrow;
		this.ncol = ncol;
	}

	@Override
	public String toString() {
		String itemlayouttring = "";
		for (int i = 0; i < rsize(); i++) {
			for (int j = 0; j < csize(); j++) {
				itemlayouttring += getValue(i, j);
				itemlayouttring += " ";
			}
			itemlayouttring += "\n";
		}
		return itemlayouttring;
	}

	public float getObjectiveValue(float[][] data) {
		float fitness = 0;
		int nrow = this.rsize();
		int ncol = this.csize();
		System.out.println("nrow:" + nrow + ", ncol:" + ncol);

		for (int i = 1; i < nrow; i++) {
			for (int j = 0; j < ncol; j++) {
				fitness += data[this.getValue(i - 1, j)][this.getValue(i, j)];
			}
		}

		for (int i = 0; i < nrow; i++) {
			for (int j = 1; j < ncol; j++) {
				// System.out.println("nrow:" + nrow + ", ncol:" + ncol + ", i:" + i + ",j:" +
				// j);

				fitness += data[this.getValue(i, j - 1)][this.getValue(i, j)];
			}
		}

		return fitness;
	}

	public void setValue(int row, int col, int value) {
		itemlayout[row][col] = value;
	}

}
