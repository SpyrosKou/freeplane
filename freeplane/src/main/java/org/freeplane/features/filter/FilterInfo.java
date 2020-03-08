/*
 *  Freeplane - mind map editor
 *  Copyright (C) 2008 Dimitry Polivaev
 *
 *  This file author is Dimitry Polivaev
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.freeplane.features.filter;

/**
 * @author Dimitry Polivaev
 */
public class FilterInfo {
	static final int FILTER_INITIAL_VALUE = 1;
	static final int FILTER_SHOW_AS_ANCESTOR = 4;
	static final int FILTER_SHOW_AS_DESCENDANT = 8;
	static final int FILTER_SHOW_AS_ECLIPSED = 16;
	static final int FILTER_SHOW_AS_HIDDEN = 32;
	static final int FILTER_SHOW_AS_MATCHED = 2;
	
	private int info = FilterInfo.FILTER_INITIAL_VALUE;

	/**
	 *
	 */
	public FilterInfo() {
		super();
	}

	void add(final int flag) {
		if ((flag & (FilterInfo.FILTER_SHOW_AS_MATCHED | FilterInfo.FILTER_SHOW_AS_HIDDEN)) != 0) {
			info &= ~FilterInfo.FILTER_INITIAL_VALUE;
		}
		info |= flag;
	}

	/**
	 */
	public boolean isAncestor() {
		return (info & FilterInfo.FILTER_SHOW_AS_ANCESTOR) != 0;
	}

	/**
	 */
	public boolean isMatched() {
		return (info & FilterInfo.FILTER_SHOW_AS_MATCHED) != 0;
	}

	public void reset() {
		info = FilterInfo.FILTER_INITIAL_VALUE;
	}

	public void setAncestor() {
		add(FilterInfo.FILTER_SHOW_AS_ANCESTOR);
	}

	public void setDescendant() {
		add(FilterInfo.FILTER_SHOW_AS_DESCENDANT);
	}

	public void setMatched() {
		add(FilterInfo.FILTER_SHOW_AS_MATCHED);
	}

	public boolean isUnset() {
		return info == FilterInfo.FILTER_INITIAL_VALUE;
	}

	boolean isVisible(final int filterOptions) {
		final boolean showAsAncestor = (filterOptions & FilterInfo.FILTER_SHOW_AS_ANCESTOR) != 0;
		return (showAsAncestor || (filterOptions & FilterInfo.FILTER_SHOW_AS_ECLIPSED) >= (info & FilterInfo.FILTER_SHOW_AS_ECLIPSED))
		        && ((filterOptions & info & ~FilterInfo.FILTER_SHOW_AS_ECLIPSED) != 0);
	}
}
