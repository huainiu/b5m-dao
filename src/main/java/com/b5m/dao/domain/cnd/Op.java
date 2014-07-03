package com.b5m.dao.domain.cnd;

public enum Op {
	EQ {
		@Override
		public String toString() {
			return "=";
		}
	},
	LIKE {
		@Override
		public String toString() {
			return "like";
		}
	},
	GT {
		@Override
		public String toString() {
			return ">";
		}
	},
	LT {
		@Override
		public String toString() {
			return "<";
		}
	},
	GTE {
		@Override
		public String toString() {
			return ">=";
		}
	},
	LTE {
		@Override
		public String toString() {
			return "<=";
		}
	},
	NEQ {
		@Override
		public String toString() {
			return "!=";
		}
	},
	ISNULL {
		@Override
		public String toString() {
			return " is null";
		}
	},
	ISNOTNULL {
		@Override
		public String toString() {
			return "is not null";
		}
	},
	BETWEEN {
		@Override
		public String toString() {
			return "between";
		}
	},
	IN{
		@Override
		public String toString() {
			return "in";
		}
	}

}
