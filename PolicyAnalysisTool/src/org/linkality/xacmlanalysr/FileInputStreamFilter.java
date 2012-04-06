package org.linkality.xacmlanalysr;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;


/**
 * Filtered InputStream - extends InputStream by adding replace function:
 * All keys from Hashtable filter are replaced by their values.
 *
 * @author   Alexander Feldman (updated by $Author: feldman $)
 * @created  2002-05-22 Alexander Feldman
 * @version  $Revision: 1.2 $
 *
 */
public class FileInputStreamFilter extends FileInputStream
{
  /** Buff with whole file read there. */
  String buff = "";

  /** Our pointer at buff for next read operation. */
  int buffOff = 0;

  /** Length of buff. */
  int buffLen = 0;


  /**
   * Constructor for Filtering InputStreams.
   *
   * @param file - File name for open.
   * @param filter - Hashtable with old/new values required to be replaced.
   * @throws FileNotFoundException in case file cannot be loaded.
   *
   */
  public FileInputStreamFilter(String file, Hashtable<String, String> filter)
    throws FileNotFoundException
  {
    super(file);
    readFile();
    doFilter(filter);
    buffLen = buff.length();
  }


  /**
   * Filtering/replacing all keys by Hashtable filter to their values.
   *
   * @param filter - Hashtable with old/new values required to be replaced.
   *
   */
  private void doFilter(Hashtable filter)
  {
    Enumeration keys = filter.keys();
    while (keys.hasMoreElements())
    {
      String key = (String) keys.nextElement();
      String val = (String) filter.get(key);
      buff = buff.replace(key, val);
    }
  }


  /**
   * Reads file into our internal buff.
   * @throws FileNotFoundException in case file cannot be loaded.
   *
   */
  private void readFile() throws FileNotFoundException
  {
    try
    {
      int readed = 0;
      for (byte [] b = new byte[1000]; (readed = super.read(b)) != -1; )
      {
        buff += new String(b, 0, readed);
      }
    }
    catch (IOException e)
    {
      throw new FileNotFoundException(e.toString());
    }
  }


  /**
   * Reads one byte from input stream.
   * This transparent function actually reads from our internal buff.
   *
   * @return Next byte from input stream.
   *
   */
  public int read()
  {
    if (buffOff < buffLen)
    {
      return buff.charAt(buffOff++);
    }
    return -1;
  }


  /**
   * Reads one byte from input stream.
   * This transparent function actually reads from our internal buff.
   *
   * @param b - byte array to store read bytes to.
   * @return Number of bytes actually read from input stream.
   *
   */
  public int read(byte [] b)
  {
    return read(b, 0, b.length);
  }


  /**
   * Reads one byte from input stream.
   * This transparent function actually reads from our internal buff.
   *
   * @param b - byte array to store read bytes to.
   * @param off - starting bytes offset at 'b' to store.
   * @param len - maximum bytes to read from input stream.
   *
   * @return Number of bytes actually read from input stream.
   *
   */
  public int read(byte [] b, int off, int len)
  {
    if (buffOff < buffLen)
    {
      int size = Math.min(buffLen - buffOff, len);
      System.arraycopy(buff.substring(buffOff, buffOff + size).getBytes(),
        0, b, off, size);
      buffOff += size;
      return size;
    }
    return -1;
  }

}
