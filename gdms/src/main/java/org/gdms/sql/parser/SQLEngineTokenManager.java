/* Generated By:JJTree&JavaCC: Do not edit this line. SQLEngineTokenManager.java */
package org.gdms.sql.parser;

public class SQLEngineTokenManager implements SQLEngineConstants {
	public java.io.PrintStream debugStream = System.out;

	public void setDebugStream(java.io.PrintStream ds) {
		debugStream = ds;
	}

	private final int jjStopStringLiteralDfa_0(int pos, long active0,
			long active1) {
		switch (pos) {
		case 0:
			if ((active1 & 0x1L) != 0L)
				return 0;
			if ((active0 & 0x2000000000000L) != 0L)
				return 14;
			if ((active0 & 0x4000000000000000L) != 0L)
				return 6;
			if ((active0 & 0x7fffffff80L) != 0L) {
				jjmatchedKind = 43;
				return 48;
			}
			if ((active0 & 0x160000000000000L) != 0L)
				return 27;
			if ((active0 & 0x218000000000000L) != 0L)
				return 31;
			return -1;
		case 1:
			if ((active0 & 0xc06302600L) != 0L)
				return 48;
			if ((active0 & 0x73f9cfd980L) != 0L) {
				if (jjmatchedPos != 1) {
					jjmatchedKind = 43;
					jjmatchedPos = 1;
				}
				return 48;
			}
			return -1;
		case 2:
			if ((active0 & 0x6ffd4fd800L) != 0L) {
				jjmatchedKind = 43;
				jjmatchedPos = 2;
				return 48;
			}
			if ((active0 & 0x1000800580L) != 0L)
				return 48;
			return -1;
		case 3:
			if ((active0 & 0x801428000L) != 0L)
				return 48;
			if ((active0 & 0x67fc0d5800L) != 0L) {
				jjmatchedKind = 43;
				jjmatchedPos = 3;
				return 48;
			}
			return -1;
		case 4:
			if ((active0 & 0x94040800L) != 0L)
				return 48;
			if ((active0 & 0x6768095000L) != 0L) {
				jjmatchedKind = 43;
				jjmatchedPos = 4;
				return 48;
			}
			return -1;
		case 5:
			if ((active0 & 0x11000L) != 0L) {
				jjmatchedKind = 43;
				jjmatchedPos = 5;
				return 48;
			}
			if ((active0 & 0x6768084000L) != 0L)
				return 48;
			return -1;
		case 6:
			if ((active0 & 0x1000L) != 0L)
				return 48;
			if ((active0 & 0x10000L) != 0L) {
				jjmatchedKind = 43;
				jjmatchedPos = 6;
				return 48;
			}
			return -1;
		default:
			return -1;
		}
	}

	private final int jjStartNfa_0(int pos, long active0, long active1) {
		return jjMoveNfa_0(jjStopStringLiteralDfa_0(pos, active0, active1),
				pos + 1);
	}

	private final int jjStopAtPos(int pos, int kind) {
		jjmatchedKind = kind;
		jjmatchedPos = pos;
		return pos + 1;
	}

	private final int jjStartNfaWithStates_0(int pos, int kind, int state) {
		jjmatchedKind = kind;
		jjmatchedPos = pos;
		try {
			curChar = input_stream.readChar();
		} catch (java.io.IOException e) {
			return pos + 1;
		}
		return jjMoveNfa_0(state, pos + 1);
	}

	private final int jjMoveStringLiteralDfa0_0() {
		switch (curChar) {
		case 33:
			return jjMoveStringLiteralDfa1_0(0x100000000000000L);
		case 40:
			jjmatchedKind = 59;
			return jjMoveStringLiteralDfa1_0(0x400000000000000L);
		case 41:
			return jjStopAtPos(0, 60);
		case 42:
			return jjStopAtPos(0, 61);
		case 43:
			return jjStopAtPos(0, 63);
		case 44:
			return jjStopAtPos(0, 67);
		case 45:
			return jjStartNfaWithStates_0(0, 64, 0);
		case 46:
			return jjStartNfaWithStates_0(0, 49, 14);
		case 47:
			return jjStartNfaWithStates_0(0, 62, 6);
		case 58:
			return jjMoveStringLiteralDfa1_0(0x400000000000L);
		case 59:
			return jjStopAtPos(0, 48);
		case 60:
			jjmatchedKind = 51;
			return jjMoveStringLiteralDfa1_0(0x210000000000000L);
		case 61:
			return jjStopAtPos(0, 55);
		case 62:
			jjmatchedKind = 53;
			return jjMoveStringLiteralDfa1_0(0x40000000000000L);
		case 63:
			return jjStopAtPos(0, 65);
		case 97:
			return jjMoveStringLiteralDfa1_0(0x780L);
		case 98:
			return jjMoveStringLiteralDfa1_0(0x3800L);
		case 99:
			return jjMoveStringLiteralDfa1_0(0x4000L);
		case 100:
			return jjMoveStringLiteralDfa1_0(0x100018000L);
		case 101:
			return jjMoveStringLiteralDfa1_0(0x200000000L);
		case 102:
			return jjMoveStringLiteralDfa1_0(0x20000L);
		case 103:
			return jjMoveStringLiteralDfa1_0(0x40000L);
		case 104:
			return jjMoveStringLiteralDfa1_0(0x80000L);
		case 105:
			return jjMoveStringLiteralDfa1_0(0xc00300000L);
		case 108:
			return jjMoveStringLiteralDfa1_0(0x400000L);
		case 110:
			return jjMoveStringLiteralDfa1_0(0x1800000L);
		case 111:
			return jjMoveStringLiteralDfa1_0(0x6000000L);
		case 115:
			return jjMoveStringLiteralDfa1_0(0x1028000000L);
		case 116:
			return jjMoveStringLiteralDfa1_0(0x40000000L);
		case 117:
			return jjMoveStringLiteralDfa1_0(0x2010000000L);
		case 118:
			return jjMoveStringLiteralDfa1_0(0x4000000000L);
		case 119:
			return jjMoveStringLiteralDfa1_0(0x80000000L);
		case 124:
			return jjMoveStringLiteralDfa1_0(0x800000000000L);
		case 126:
			return jjStopAtPos(0, 50);
		default:
			return jjMoveNfa_0(5, 0);
		}
	}

	private final int jjMoveStringLiteralDfa1_0(long active0) {
		try {
			curChar = input_stream.readChar();
		} catch (java.io.IOException e) {
			jjStopStringLiteralDfa_0(0, active0, 0L);
			return 1;
		}
		switch (curChar) {
		case 43:
			return jjMoveStringLiteralDfa2_0(active0, 0x400000000000000L);
		case 61:
			if ((active0 & 0x400000000000L) != 0L)
				return jjStopAtPos(1, 46);
			else if ((active0 & 0x10000000000000L) != 0L)
				return jjStopAtPos(1, 52);
			else if ((active0 & 0x40000000000000L) != 0L)
				return jjStopAtPos(1, 54);
			else if ((active0 & 0x100000000000000L) != 0L)
				return jjStopAtPos(1, 56);
			break;
		case 62:
			if ((active0 & 0x200000000000000L) != 0L)
				return jjStopAtPos(1, 57);
			break;
		case 97:
			return jjMoveStringLiteralDfa2_0(active0, 0x4040080000L);
		case 101:
			return jjMoveStringLiteralDfa2_0(active0, 0x1108009800L);
		case 104:
			return jjMoveStringLiteralDfa2_0(active0, 0x80000000L);
		case 105:
			return jjMoveStringLiteralDfa2_0(active0, 0x410000L);
		case 108:
			return jjMoveStringLiteralDfa2_0(active0, 0x80L);
		case 110:
			if ((active0 & 0x100000L) != 0L) {
				jjmatchedKind = 20;
				jjmatchedPos = 1;
			}
			return jjMoveStringLiteralDfa2_0(active0, 0xc10000100L);
		case 111:
			return jjMoveStringLiteralDfa2_0(active0, 0x800000L);
		case 112:
			return jjMoveStringLiteralDfa2_0(active0, 0x2020000000L);
		case 114:
			if ((active0 & 0x2000000L) != 0L) {
				jjmatchedKind = 25;
				jjmatchedPos = 1;
			}
			return jjMoveStringLiteralDfa2_0(active0, 0x4060000L);
		case 115:
			if ((active0 & 0x200L) != 0L) {
				jjmatchedKind = 9;
				jjmatchedPos = 1;
			} else if ((active0 & 0x200000L) != 0L)
				return jjStartNfaWithStates_0(1, 21, 48);
			return jjMoveStringLiteralDfa2_0(active0, 0x400L);
		case 117:
			return jjMoveStringLiteralDfa2_0(active0, 0x1004000L);
		case 120:
			return jjMoveStringLiteralDfa2_0(active0, 0x200000000L);
		case 121:
			if ((active0 & 0x2000L) != 0L)
				return jjStartNfaWithStates_0(1, 13, 48);
			break;
		case 124:
			if ((active0 & 0x800000000000L) != 0L)
				return jjStopAtPos(1, 47);
			break;
		default:
			break;
		}
		return jjStartNfa_0(0, active0, 0L);
	}

	private final int jjMoveStringLiteralDfa2_0(long old0, long active0) {
		if (((active0 &= old0)) == 0L)
			return jjStartNfa_0(0, old0, 0L);
		try {
			curChar = input_stream.readChar();
		} catch (java.io.IOException e) {
			jjStopStringLiteralDfa_0(1, active0, 0L);
			return 2;
		}
		switch (curChar) {
		case 41:
			if ((active0 & 0x400000000000000L) != 0L)
				return jjStopAtPos(2, 58);
			break;
		case 97:
			return jjMoveStringLiteralDfa3_0(active0, 0x20000000L);
		case 98:
			return jjMoveStringLiteralDfa3_0(active0, 0x40000000L);
		case 99:
			if ((active0 & 0x400L) != 0L)
				return jjStartNfaWithStates_0(2, 10, 48);
			break;
		case 100:
			if ((active0 & 0x100L) != 0L)
				return jjStartNfaWithStates_0(2, 8, 48);
			return jjMoveStringLiteralDfa3_0(active0, 0x2004000000L);
		case 101:
			return jjMoveStringLiteralDfa3_0(active0, 0x80000000L);
		case 103:
			return jjMoveStringLiteralDfa3_0(active0, 0x800L);
		case 105:
			return jjMoveStringLiteralDfa3_0(active0, 0x210000000L);
		case 107:
			return jjMoveStringLiteralDfa3_0(active0, 0x400000L);
		case 108:
			if ((active0 & 0x80L) != 0L)
				return jjStartNfaWithStates_0(2, 7, 48);
			return jjMoveStringLiteralDfa3_0(active0, 0x4109000000L);
		case 111:
			return jjMoveStringLiteralDfa3_0(active0, 0x60000L);
		case 115:
			return jjMoveStringLiteralDfa3_0(active0, 0x40001c000L);
		case 116:
			if ((active0 & 0x800000L) != 0L)
				return jjStartNfaWithStates_0(2, 23, 48);
			else if ((active0 & 0x1000000000L) != 0L)
				return jjStartNfaWithStates_0(2, 36, 48);
			return jjMoveStringLiteralDfa3_0(active0, 0x800001000L);
		case 118:
			return jjMoveStringLiteralDfa3_0(active0, 0x80000L);
		default:
			break;
		}
		return jjStartNfa_0(1, active0, 0L);
	}

	private final int jjMoveStringLiteralDfa3_0(long old0, long active0) {
		if (((active0 &= old0)) == 0L)
			return jjStartNfa_0(1, old0, 0L);
		try {
			curChar = input_stream.readChar();
		} catch (java.io.IOException e) {
			jjStopStringLiteralDfa_0(2, active0, 0L);
			return 3;
		}
		switch (curChar) {
		case 97:
			return jjMoveStringLiteralDfa4_0(active0, 0x2000000000L);
		case 99:
			if ((active0 & 0x8000L) != 0L)
				return jjStartNfaWithStates_0(3, 15, 48);
			return jjMoveStringLiteralDfa4_0(active0, 0x20000000L);
		case 101:
			if ((active0 & 0x400000L) != 0L)
				return jjStartNfaWithStates_0(3, 22, 48);
			return jjMoveStringLiteralDfa4_0(active0, 0x50c000000L);
		case 105:
			return jjMoveStringLiteralDfa4_0(active0, 0x80800L);
		case 108:
			if ((active0 & 0x1000000L) != 0L)
				return jjStartNfaWithStates_0(3, 24, 48);
			return jjMoveStringLiteralDfa4_0(active0, 0x40000000L);
		case 109:
			if ((active0 & 0x20000L) != 0L)
				return jjStartNfaWithStates_0(3, 17, 48);
			break;
		case 111:
			if ((active0 & 0x800000000L) != 0L)
				return jjStartNfaWithStates_0(3, 35, 48);
			return jjMoveStringLiteralDfa4_0(active0, 0x10000000L);
		case 114:
			return jjMoveStringLiteralDfa4_0(active0, 0x80000000L);
		case 115:
			return jjMoveStringLiteralDfa4_0(active0, 0x200000000L);
		case 116:
			return jjMoveStringLiteralDfa4_0(active0, 0x14000L);
		case 117:
			return jjMoveStringLiteralDfa4_0(active0, 0x4000040000L);
		case 119:
			return jjMoveStringLiteralDfa4_0(active0, 0x1000L);
		default:
			break;
		}
		return jjStartNfa_0(2, active0, 0L);
	}

	private final int jjMoveStringLiteralDfa4_0(long old0, long active0) {
		if (((active0 &= old0)) == 0L)
			return jjStartNfa_0(2, old0, 0L);
		try {
			curChar = input_stream.readChar();
		} catch (java.io.IOException e) {
			jjStopStringLiteralDfa_0(3, active0, 0L);
			return 4;
		}
		switch (curChar) {
		case 99:
			return jjMoveStringLiteralDfa5_0(active0, 0x8000000L);
		case 101:
			if ((active0 & 0x80000000L) != 0L)
				return jjStartNfaWithStates_0(4, 31, 48);
			return jjMoveStringLiteralDfa5_0(active0, 0x4060001000L);
		case 105:
			return jjMoveStringLiteralDfa5_0(active0, 0x10000L);
		case 110:
			if ((active0 & 0x800L) != 0L)
				return jjStartNfaWithStates_0(4, 11, 48);
			else if ((active0 & 0x10000000L) != 0L)
				return jjStartNfaWithStates_0(4, 28, 48);
			return jjMoveStringLiteralDfa5_0(active0, 0x80000L);
		case 111:
			return jjMoveStringLiteralDfa5_0(active0, 0x4000L);
		case 112:
			if ((active0 & 0x40000L) != 0L)
				return jjStartNfaWithStates_0(4, 18, 48);
			break;
		case 114:
			if ((active0 & 0x4000000L) != 0L)
				return jjStartNfaWithStates_0(4, 26, 48);
			return jjMoveStringLiteralDfa5_0(active0, 0x400000000L);
		case 116:
			return jjMoveStringLiteralDfa5_0(active0, 0x2300000000L);
		default:
			break;
		}
		return jjStartNfa_0(3, active0, 0L);
	}

	private final int jjMoveStringLiteralDfa5_0(long old0, long active0) {
		if (((active0 &= old0)) == 0L)
			return jjStartNfa_0(3, old0, 0L);
		try {
			curChar = input_stream.readChar();
		} catch (java.io.IOException e) {
			jjStopStringLiteralDfa_0(4, active0, 0L);
			return 5;
		}
		switch (curChar) {
		case 101:
			if ((active0 & 0x100000000L) != 0L)
				return jjStartNfaWithStates_0(5, 32, 48);
			else if ((active0 & 0x2000000000L) != 0L)
				return jjStartNfaWithStates_0(5, 37, 48);
			return jjMoveStringLiteralDfa6_0(active0, 0x1000L);
		case 103:
			if ((active0 & 0x80000L) != 0L)
				return jjStartNfaWithStates_0(5, 19, 48);
			break;
		case 109:
			if ((active0 & 0x4000L) != 0L)
				return jjStartNfaWithStates_0(5, 14, 48);
			break;
		case 110:
			return jjMoveStringLiteralDfa6_0(active0, 0x10000L);
		case 115:
			if ((active0 & 0x20000000L) != 0L)
				return jjStartNfaWithStates_0(5, 29, 48);
			else if ((active0 & 0x40000000L) != 0L)
				return jjStartNfaWithStates_0(5, 30, 48);
			else if ((active0 & 0x200000000L) != 0L)
				return jjStartNfaWithStates_0(5, 33, 48);
			else if ((active0 & 0x4000000000L) != 0L)
				return jjStartNfaWithStates_0(5, 38, 48);
			break;
		case 116:
			if ((active0 & 0x8000000L) != 0L)
				return jjStartNfaWithStates_0(5, 27, 48);
			else if ((active0 & 0x400000000L) != 0L)
				return jjStartNfaWithStates_0(5, 34, 48);
			break;
		default:
			break;
		}
		return jjStartNfa_0(4, active0, 0L);
	}

	private final int jjMoveStringLiteralDfa6_0(long old0, long active0) {
		if (((active0 &= old0)) == 0L)
			return jjStartNfa_0(4, old0, 0L);
		try {
			curChar = input_stream.readChar();
		} catch (java.io.IOException e) {
			jjStopStringLiteralDfa_0(5, active0, 0L);
			return 6;
		}
		switch (curChar) {
		case 99:
			return jjMoveStringLiteralDfa7_0(active0, 0x10000L);
		case 110:
			if ((active0 & 0x1000L) != 0L)
				return jjStartNfaWithStates_0(6, 12, 48);
			break;
		default:
			break;
		}
		return jjStartNfa_0(5, active0, 0L);
	}

	private final int jjMoveStringLiteralDfa7_0(long old0, long active0) {
		if (((active0 &= old0)) == 0L)
			return jjStartNfa_0(5, old0, 0L);
		try {
			curChar = input_stream.readChar();
		} catch (java.io.IOException e) {
			jjStopStringLiteralDfa_0(6, active0, 0L);
			return 7;
		}
		switch (curChar) {
		case 116:
			if ((active0 & 0x10000L) != 0L)
				return jjStartNfaWithStates_0(7, 16, 48);
			break;
		default:
			break;
		}
		return jjStartNfa_0(6, active0, 0L);
	}

	private final void jjCheckNAdd(int state) {
		if (jjrounds[state] != jjround) {
			jjstateSet[jjnewStateCnt++] = state;
			jjrounds[state] = jjround;
		}
	}

	private final void jjAddStates(int start, int end) {
		do {
			jjstateSet[jjnewStateCnt++] = jjnextStates[start];
		} while (start++ != end);
	}

	private final void jjCheckNAddTwoStates(int state1, int state2) {
		jjCheckNAdd(state1);
		jjCheckNAdd(state2);
	}

	private final void jjCheckNAddStates(int start, int end) {
		do {
			jjCheckNAdd(jjnextStates[start]);
		} while (start++ != end);
	}

	private final void jjCheckNAddStates(int start) {
		jjCheckNAdd(jjnextStates[start]);
		jjCheckNAdd(jjnextStates[start + 1]);
	}

	static final long[] jjbitVec0 = { 0x0L, 0x0L, 0xffffffffffffffffL,
			0xffffffffffffffffL };

	private final int jjMoveNfa_0(int startState, int curPos) {
		int[] nextStates;
		int startsAt = 0;
		jjnewStateCnt = 48;
		int i = 1;
		jjstateSet[0] = startState;
		int j, kind = 0x7fffffff;
		for (;;) {
			if (++jjround == 0x7fffffff)
				ReInitRounds();
			if (curChar < 64) {
				long l = 1L << curChar;
				MatchLoop: do {
					switch (jjstateSet[--i]) {
					case 5:
						if ((0x3ff000000000000L & l) != 0L) {
							if (kind > 39)
								kind = 39;
							jjCheckNAddStates(0, 6);
						} else if ((0x7000000000000000L & l) != 0L) {
							if (kind > 66)
								kind = 66;
						} else if (curChar == 33)
							jjCheckNAdd(27);
						else if (curChar == 39)
							jjCheckNAddStates(7, 9);
						else if (curChar == 46)
							jjCheckNAdd(14);
						else if (curChar == 47)
							jjstateSet[jjnewStateCnt++] = 6;
						else if (curChar == 45)
							jjstateSet[jjnewStateCnt++] = 0;
						if (curChar == 60)
							jjCheckNAddTwoStates(27, 31);
						else if (curChar == 62)
							jjCheckNAdd(27);
						break;
					case 31:
						if (curChar == 62) {
							if (kind > 66)
								kind = 66;
						} else if (curChar == 61) {
							if (kind > 66)
								kind = 66;
						}
						break;
					case 48:
					case 25:
						if ((0x3ff001800000000L & l) == 0L)
							break;
						if (kind > 43)
							kind = 43;
						jjCheckNAdd(25);
						break;
					case 0:
						if (curChar == 45)
							jjCheckNAddStates(10, 12);
						break;
					case 1:
						if ((0xffffffffffffdbffL & l) != 0L)
							jjCheckNAddStates(10, 12);
						break;
					case 2:
						if ((0x2400L & l) != 0L && kind > 5)
							kind = 5;
						break;
					case 3:
						if (curChar == 10 && kind > 5)
							kind = 5;
						break;
					case 4:
						if (curChar == 13)
							jjstateSet[jjnewStateCnt++] = 3;
						break;
					case 6:
						if (curChar == 42)
							jjCheckNAddTwoStates(7, 8);
						break;
					case 7:
						if ((0xfffffbffffffffffL & l) != 0L)
							jjCheckNAddTwoStates(7, 8);
						break;
					case 8:
						if (curChar == 42)
							jjCheckNAddStates(13, 15);
						break;
					case 9:
						if ((0xffff7bffffffffffL & l) != 0L)
							jjCheckNAddTwoStates(10, 8);
						break;
					case 10:
						if ((0xfffffbffffffffffL & l) != 0L)
							jjCheckNAddTwoStates(10, 8);
						break;
					case 11:
						if (curChar == 47 && kind > 6)
							kind = 6;
						break;
					case 12:
						if (curChar == 47)
							jjstateSet[jjnewStateCnt++] = 6;
						break;
					case 13:
						if (curChar == 46)
							jjCheckNAdd(14);
						break;
					case 14:
						if ((0x3ff000000000000L & l) == 0L)
							break;
						if (kind > 40)
							kind = 40;
						jjCheckNAddTwoStates(14, 15);
						break;
					case 16:
						if ((0x280000000000L & l) != 0L)
							jjCheckNAdd(17);
						break;
					case 17:
						if ((0x3ff000000000000L & l) == 0L)
							break;
						if (kind > 40)
							kind = 40;
						jjCheckNAdd(17);
						break;
					case 18:
						if (curChar == 39)
							jjCheckNAddStates(7, 9);
						break;
					case 19:
						if ((0xffffff7fffffffffL & l) != 0L)
							jjCheckNAddStates(7, 9);
						break;
					case 20:
						if (curChar == 39)
							jjCheckNAddStates(16, 18);
						break;
					case 21:
						if (curChar == 39)
							jjstateSet[jjnewStateCnt++] = 20;
						break;
					case 22:
						if ((0xffffff7fffffffffL & l) != 0L)
							jjCheckNAddStates(16, 18);
						break;
					case 23:
						if (curChar == 39 && kind > 42)
							kind = 42;
						break;
					case 26:
						if ((0x7000000000000000L & l) != 0L && kind > 66)
							kind = 66;
						break;
					case 27:
						if (curChar == 61 && kind > 66)
							kind = 66;
						break;
					case 28:
						if (curChar == 62)
							jjCheckNAdd(27);
						break;
					case 29:
						if (curChar == 33)
							jjCheckNAdd(27);
						break;
					case 30:
						if (curChar == 60)
							jjCheckNAddTwoStates(27, 31);
						break;
					case 32:
						if ((0x3ff000000000000L & l) == 0L)
							break;
						if (kind > 39)
							kind = 39;
						jjCheckNAddStates(0, 6);
						break;
					case 33:
						if ((0x3ff000000000000L & l) == 0L)
							break;
						if (kind > 39)
							kind = 39;
						jjCheckNAdd(33);
						break;
					case 34:
						if ((0x3ff000000000000L & l) != 0L)
							jjCheckNAddTwoStates(34, 35);
						break;
					case 35:
						if (curChar == 46)
							jjCheckNAdd(36);
						break;
					case 36:
						if ((0x3ff000000000000L & l) == 0L)
							break;
						if (kind > 40)
							kind = 40;
						jjCheckNAddTwoStates(36, 37);
						break;
					case 38:
						if ((0x280000000000L & l) != 0L)
							jjCheckNAdd(39);
						break;
					case 39:
						if ((0x3ff000000000000L & l) == 0L)
							break;
						if (kind > 40)
							kind = 40;
						jjCheckNAdd(39);
						break;
					case 40:
						if ((0x3ff000000000000L & l) != 0L)
							jjCheckNAddTwoStates(40, 41);
						break;
					case 42:
						if ((0x280000000000L & l) != 0L)
							jjCheckNAdd(43);
						break;
					case 43:
						if ((0x3ff000000000000L & l) == 0L)
							break;
						if (kind > 40)
							kind = 40;
						jjCheckNAdd(43);
						break;
					case 44:
						if ((0x3ff000000000000L & l) == 0L)
							break;
						if (kind > 40)
							kind = 40;
						jjCheckNAddTwoStates(44, 45);
						break;
					case 46:
						if ((0x280000000000L & l) != 0L)
							jjCheckNAdd(47);
						break;
					case 47:
						if ((0x3ff000000000000L & l) == 0L)
							break;
						if (kind > 40)
							kind = 40;
						jjCheckNAdd(47);
						break;
					default:
						break;
					}
				} while (i != startsAt);
			} else if (curChar < 128) {
				long l = 1L << (curChar & 077);
				MatchLoop: do {
					switch (jjstateSet[--i]) {
					case 5:
					case 24:
						if ((0x7fffffe07fffffeL & l) == 0L)
							break;
						if (kind > 43)
							kind = 43;
						jjCheckNAddTwoStates(24, 25);
						break;
					case 48:
						if ((0x7fffffe87fffffeL & l) != 0L) {
							if (kind > 43)
								kind = 43;
							jjCheckNAdd(25);
						}
						if ((0x7fffffe07fffffeL & l) != 0L) {
							if (kind > 43)
								kind = 43;
							jjCheckNAddTwoStates(24, 25);
						}
						break;
					case 1:
						jjAddStates(10, 12);
						break;
					case 7:
						jjCheckNAddTwoStates(7, 8);
						break;
					case 9:
					case 10:
						jjCheckNAddTwoStates(10, 8);
						break;
					case 15:
						if ((0x2000000020L & l) != 0L)
							jjAddStates(19, 20);
						break;
					case 19:
						jjCheckNAddStates(7, 9);
						break;
					case 22:
						jjCheckNAddStates(16, 18);
						break;
					case 25:
						if ((0x7fffffe87fffffeL & l) == 0L)
							break;
						if (kind > 43)
							kind = 43;
						jjCheckNAdd(25);
						break;
					case 37:
						if ((0x2000000020L & l) != 0L)
							jjAddStates(21, 22);
						break;
					case 41:
						if ((0x2000000020L & l) != 0L)
							jjAddStates(23, 24);
						break;
					case 45:
						if ((0x2000000020L & l) != 0L)
							jjAddStates(25, 26);
						break;
					default:
						break;
					}
				} while (i != startsAt);
			} else {
				int i2 = (curChar & 0xff) >> 6;
				long l2 = 1L << (curChar & 077);
				MatchLoop: do {
					switch (jjstateSet[--i]) {
					case 1:
						if ((jjbitVec0[i2] & l2) != 0L)
							jjAddStates(10, 12);
						break;
					case 7:
						if ((jjbitVec0[i2] & l2) != 0L)
							jjCheckNAddTwoStates(7, 8);
						break;
					case 9:
					case 10:
						if ((jjbitVec0[i2] & l2) != 0L)
							jjCheckNAddTwoStates(10, 8);
						break;
					case 19:
						if ((jjbitVec0[i2] & l2) != 0L)
							jjCheckNAddStates(7, 9);
						break;
					case 22:
						if ((jjbitVec0[i2] & l2) != 0L)
							jjCheckNAddStates(16, 18);
						break;
					default:
						break;
					}
				} while (i != startsAt);
			}
			if (kind != 0x7fffffff) {
				jjmatchedKind = kind;
				jjmatchedPos = curPos;
				kind = 0x7fffffff;
			}
			++curPos;
			if ((i = jjnewStateCnt) == (startsAt = 48 - (jjnewStateCnt = startsAt)))
				return curPos;
			try {
				curChar = input_stream.readChar();
			} catch (java.io.IOException e) {
				return curPos;
			}
		}
	}

	static final int[] jjnextStates = { 33, 34, 35, 40, 41, 44, 45, 19, 21, 23,
			1, 2, 4, 8, 9, 11, 21, 22, 23, 16, 17, 38, 39, 42, 43, 46, 47, };

	public static final String[] jjstrLiteralImages = { "", null, null, null,
			null, null, null, "\141\154\154", "\141\156\144", "\141\163",
			"\141\163\143", "\142\145\147\151\156",
			"\142\145\164\167\145\145\156", "\142\171",
			"\143\165\163\164\157\155", "\144\145\163\143",
			"\144\151\163\164\151\156\143\164", "\146\162\157\155",
			"\147\162\157\165\160", "\150\141\166\151\156\147", "\151\156",
			"\151\163", "\154\151\153\145", "\156\157\164", "\156\165\154\154",
			"\157\162", "\157\162\144\145\162", "\163\145\154\145\143\164",
			"\165\156\151\157\156", "\163\160\141\143\145\163",
			"\164\141\142\154\145\163", "\167\150\145\162\145",
			"\144\145\154\145\164\145", "\145\170\151\163\164\163",
			"\151\156\163\145\162\164", "\151\156\164\157", "\163\145\164",
			"\165\160\144\141\164\145", "\166\141\154\165\145\163", null, null,
			null, null, null, null, null, "\72\75", "\174\174", "\73", "\56",
			"\176", "\74", "\74\75", "\76", "\76\75", "\75", "\41\75",
			"\74\76", "\50\53\51", "\50", "\51", "\52", "\57", "\53", "\55",
			"\77", null, "\54", };

	public static final String[] lexStateNames = { "DEFAULT", };

	static final long[] jjtoToken = { 0xffffcdffffffff81L, 0xfL, };

	static final long[] jjtoSkip = { 0x7eL, 0x0L, };

	protected SimpleCharStream input_stream;

	private final int[] jjrounds = new int[48];

	private final int[] jjstateSet = new int[96];

	protected char curChar;

	public SQLEngineTokenManager(SimpleCharStream stream) {
		if (SimpleCharStream.staticFlag)
			throw new Error(
					"ERROR: Cannot use a static CharStream class with a non-static lexical analyzer.");
		input_stream = stream;
	}

	public SQLEngineTokenManager(SimpleCharStream stream, int lexState) {
		this(stream);
		SwitchTo(lexState);
	}

	public void ReInit(SimpleCharStream stream) {
		jjmatchedPos = jjnewStateCnt = 0;
		curLexState = defaultLexState;
		input_stream = stream;
		ReInitRounds();
	}

	private final void ReInitRounds() {
		int i;
		jjround = 0x80000001;
		for (i = 48; i-- > 0;)
			jjrounds[i] = 0x80000000;
	}

	public void ReInit(SimpleCharStream stream, int lexState) {
		ReInit(stream);
		SwitchTo(lexState);
	}

	public void SwitchTo(int lexState) {
		if (lexState >= 1 || lexState < 0)
			throw new TokenMgrError("Error: Ignoring invalid lexical state : "
					+ lexState + ". State unchanged.",
					TokenMgrError.INVALID_LEXICAL_STATE);
		else
			curLexState = lexState;
	}

	protected Token jjFillToken() {
		Token t = Token.newToken(jjmatchedKind);
		t.kind = jjmatchedKind;
		String im = jjstrLiteralImages[jjmatchedKind];
		t.image = (im == null) ? input_stream.GetImage() : im;
		t.beginLine = input_stream.getBeginLine();
		t.beginColumn = input_stream.getBeginColumn();
		t.endLine = input_stream.getEndLine();
		t.endColumn = input_stream.getEndColumn();
		return t;
	}

	int curLexState = 0;

	int defaultLexState = 0;

	int jjnewStateCnt;

	int jjround;

	int jjmatchedPos;

	int jjmatchedKind;

	public Token getNextToken() {
		int kind;
		Token specialToken = null;
		Token matchedToken;
		int curPos = 0;

		EOFLoop: for (;;) {
			try {
				curChar = input_stream.BeginToken();
			} catch (java.io.IOException e) {
				jjmatchedKind = 0;
				matchedToken = jjFillToken();
				return matchedToken;
			}

			try {
				input_stream.backup(0);
				while (curChar <= 32 && (0x100002600L & (1L << curChar)) != 0L)
					curChar = input_stream.BeginToken();
			} catch (java.io.IOException e1) {
				continue EOFLoop;
			}
			jjmatchedKind = 0x7fffffff;
			jjmatchedPos = 0;
			curPos = jjMoveStringLiteralDfa0_0();
			if (jjmatchedKind != 0x7fffffff) {
				if (jjmatchedPos + 1 < curPos)
					input_stream.backup(curPos - jjmatchedPos - 1);
				if ((jjtoToken[jjmatchedKind >> 6] & (1L << (jjmatchedKind & 077))) != 0L) {
					matchedToken = jjFillToken();
					return matchedToken;
				} else {
					continue EOFLoop;
				}
			}
			int error_line = input_stream.getEndLine();
			int error_column = input_stream.getEndColumn();
			String error_after = null;
			boolean EOFSeen = false;
			try {
				input_stream.readChar();
				input_stream.backup(1);
			} catch (java.io.IOException e1) {
				EOFSeen = true;
				error_after = curPos <= 1 ? "" : input_stream.GetImage();
				if (curChar == '\n' || curChar == '\r') {
					error_line++;
					error_column = 0;
				} else
					error_column++;
			}
			if (!EOFSeen) {
				input_stream.backup(1);
				error_after = curPos <= 1 ? "" : input_stream.GetImage();
			}
			throw new TokenMgrError(EOFSeen, curLexState, error_line,
					error_column, error_after, curChar,
					TokenMgrError.LEXICAL_ERROR);
		}
	}

}
