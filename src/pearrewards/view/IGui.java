/*
 * Copyright (C) 2021 PereCraft
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package pearrewards.view;

/**
 *
 * @author antonio
 */
public interface IGui {
    
    /**
     * Open the menu of the gui.
     * @param numRewards that the player had to claim.
     */
    public void openMenu(String numRewards);
    
    /**
     * Open a menu of the gui to warn player that he had just claimed the reward.
     * @param message of warining.
     */
    public void usedRewardMenu(String message);

}
