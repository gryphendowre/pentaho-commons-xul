/*!
 * This program is free software; you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License, version 2.1 as published by the Free Software
 * Foundation.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this
 * program; if not, you can obtain a copy at http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 * or from the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * Copyright (c) 2002-2017 Hitachi Vantara..  All rights reserved.
 */

package org.pentaho.ui.xul.util;

import org.pentaho.ui.xul.XulEventSourceAdapter;
import org.pentaho.ui.xul.containers.XulManagedCollection;
import org.pentaho.ui.xul.stereotype.Bindable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Base class for UI model objects that provides {@see java.util.List} implementations and XulEventSource support
 *
 * @param <Object>
 *          type of children
 */
public class AbstractModelList<Object> extends XulEventSourceAdapter implements List<java.lang.Object>, Iterable<java.lang.Object>, Serializable,
  XulManagedCollection {

  protected List<java.lang.Object> children = new ArrayList<>();

  public AbstractModelList() {
  }

  public AbstractModelList( List<java.lang.Object> children ) {
    this.children = new ArrayList<>( children );
  }

  @Bindable
  public List<java.lang.Object> getChildren() {
    return this;
  }

  @Bindable
  public void setChildren( List<java.lang.Object> children ) {
    this.children.clear();
    this.children.addAll( children );
    fireCollectionChanged();
  }

  protected void fireCollectionChanged() {
    firePropertyChange( "children", null, this.getChildren() );
  }

  public boolean add( java.lang.Object child ) {
    boolean retVal = this.children.add( child );
    onAdd( child );
    fireCollectionChanged();
    return retVal;
  }

  public java.lang.Object remove( int idx ) {
  java.lang.Object t = children.remove( idx );
    onRemove( t );
    fireCollectionChanged();
    return t;
  }

  public boolean remove( java.lang.Object child ) {
    if ( !this.children.contains( child ) ) {
      throw new IllegalArgumentException( "Child does not exist in collection" );
    }
    boolean retVal = this.children.remove( child );
    if ( retVal ) {
      onRemove( child );
    }
    fireCollectionChanged();
    return retVal;
  }

  public java.lang.Object removeModel( int pos ) {
    if ( pos > this.children.size() ) {
      throw new IllegalArgumentException( "Specified position (" + pos + ") is greater than collection length" );
    }
  java.lang.Object retVal = this.children.remove( pos );
    onRemove( retVal );
    fireCollectionChanged();
    return retVal;
  }

  public Iterator<java.lang.Object> iterator() {
    return this.children.iterator();
  }

  public void clear() {
    for ( java.lang.Object t : this.children ) {
      onRemove( t );
    }
    this.children.clear();
    fireCollectionChanged();
  }

  public void moveChildUp( java.lang.Object column ) {
    if ( !this.children.contains( column ) ) {
      throw new IllegalArgumentException( "child does not exist in collection" );
    }

    int pos = this.children.indexOf( column );
    moveChildUp( pos );
  }

  public void moveChildUp( int position ) {
    if ( position - 1 < 0 ) {
      throw new IllegalArgumentException( "Specified position (" + position
        + ") is greater than child collection length" );
    }
    // If already at Beginning do nothing
    if ( position == 0 ) {
      return;
    }
  java.lang.Object child = this.children.remove( position );
    this.children.add( position - 1, child );
    fireCollectionChanged();
  }

  public void moveChildDown( java.lang.Object column ) {
    if ( !this.children.contains( column ) ) {
      throw new IllegalArgumentException( "child does not exist in collection" );
    }

    int pos = this.children.indexOf( column );
    moveChildDown( pos );
  }

  public void moveChildDown( int position ) {
    if ( position < 0 || position + 1 >= this.children.size() ) {
      throw new IllegalArgumentException( "Specified position (" + position
        + ") is greater than child collection length" );
    }

    java.lang.Object child = this.children.remove( position );
    this.children.add( position + 1, child );
    fireCollectionChanged();
  }

  public List<java.lang.Object> asList() {
    // UnmodifiableList not serializable
    // return Collections.unmodifiableList(this.children);

    return this.children;
  }

  public boolean addAll( Collection<? extends java.lang.Object> c ) {
    boolean retVal = this.children.addAll( c );
    if ( retVal ) {
      for ( java.lang.Object t : c ) {
        onAdd( t );
      }
      fireCollectionChanged();
    }
    return retVal;
  }

  public boolean contains( java.lang.Object o ) {
    return this.children.contains( o );
  }

  public boolean containsAll( Collection<?> c ) {
    boolean retval = true;
    for ( java.lang.Object t : c ) {
      if ( this.children.contains( t ) == false ) {
        retval = false;
        break;
      }
    }
    return retval;
  }

  public boolean isEmpty() {
    return this.children.isEmpty();
  }

  public boolean removeAll( Collection<?> c ) {
    boolean retVal = this.children.removeAll( c );
    for ( java.lang.Object t : c ) {
      onRemove( t );
    }

    fireCollectionChanged();
    return retVal;
  }

  public boolean retainAll( Collection<?> c ) {
    boolean retVal = this.children.retainAll( c );

    fireCollectionChanged();
    return retVal;
  }

  public int size() {
    return this.children.size();
  }

  public java.lang.Object[] toArray() {
    return this.children.toArray();
  }

  public <T> T[] toArray( T[] a ) {
    return this.children.toArray( a );
  }

  public void add( int index, java.lang.Object element ) {
    children.add( index, element );
    onAdd( element );
    fireCollectionChanged();
  }

  public boolean addAll( int index, Collection<? extends java.lang.Object> c ) {
    boolean retVal = children.addAll( index, c );
    if ( retVal ) {
      for ( java.lang.Object t : c ) {
        onAdd( t );
      }
    }
    fireCollectionChanged();
    return retVal;
  }

  public java.lang.Object get( int index ) {
    return children.get( index );
  }

  public int indexOf( java.lang.Object o ) {
    return children.indexOf( o );
  }

  public int lastIndexOf( java.lang.Object o ) {
    return children.lastIndexOf( o );
  }

  public ListIterator<java.lang.Object> listIterator() {
    return children.listIterator();
  }

  public ListIterator<java.lang.Object> listIterator( int index ) {
    return children.listIterator( index );
  }

  public java.lang.Object set( int index, java.lang.Object element ) {
  java.lang.Object retVal = children.set( index, element );
    fireCollectionChanged();
    return retVal;
  }

  public List<java.lang.Object> subList( int fromIndex, int toIndex ) {
    // children.subList() does not compile in GWT, re-implemented here
    List<java.lang.Object> newList = new ArrayList<>();
    for ( int i = fromIndex; i < children.size() && i < toIndex; i++ ) {
      newList.add( children.get( i ) );
    }
    return newList;
  }

  public void onAdd( java.lang.Object child ) {

  }

  public void onRemove( java.lang.Object child ) {

  }

}
