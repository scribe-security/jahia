/**
 * Jahia Enterprise Edition v6
 *
 * Copyright (C) 2002-2009 Jahia Solutions Group. All rights reserved.
 *
 * Jahia delivers the first Open Source Web Content Integration Software by combining Enterprise Web Content Management
 * with Document Management and Portal features.
 *
 * The Jahia Enterprise Edition is delivered ON AN "AS IS" BASIS, WITHOUT WARRANTY OF ANY KIND, EITHER EXPRESSED OR
 * IMPLIED.
 *
 * Jahia Enterprise Edition must be used in accordance with the terms contained in a separate license agreement between
 * you and Jahia (Jahia Sustainable Enterprise License - JSEL).
 *
 * If you are unsure which license is appropriate for your use, please contact the sales department at sales@jahia.com.
 */
/*
 * Copyright 2000-2004 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jahia.pipelines;

import org.jahia.pipelines.valves.Valve;

/**
 *
 * @author <a href="mailto:david@bluesunrise.com">David Sean Taylor</a>
 * @version $Id$
 */
public interface Pipeline {

    void initialize ()
        throws PipelineException;

    /**
     * <p>Add a new Valve to the end of the pipeline.</p>
     *
     * @param valve Valve to be added.
     *
     * @exception IllegalStateException If the pipeline has not been
     * initialized.
     */
    void addValve (Valve valve);

    /**
     * <p>Return the set of all Valves in the pipeline.  If there are no
     * such Valves, a zero-length array is returned.</p>
     *
     * @return An array of valves.
     */
    Valve[] getValves ();

    /**
     * <p>Cause the specified request and response to be processed by
     * the sequence of Valves associated with this pipeline, until one
     * of these Valves decides to end the processing.</p>
     *
     * <p>The implementation must ensure that multiple simultaneous
     * requests (on different threads) can be processed through the
     * same Pipeline without interfering with each other's control
     * flow.</p>
     *
     * @param context The run-time information, including the servlet
     * request and response we are processing.
     *
     * @exception PipelineException an input/output error occurred.
     */
    void invoke (Object context)
        throws PipelineException;

    /**
     * <p>Remove the specified Valve from the pipeline, if it is found;
     * otherwise, do nothing.</p>
     *
     * @param valve Valve to be removed.
     */
    void removeValve (Valve valve);

    // BEGIN [added by Pascal Aubry for CAS authentication]
    /**
     * <p>Tell if (at least) one of the valves of the pipeline is an instance
     * of a given class or interface.</p>
     * @param c the class or interface
     * @return
     */
    public boolean hasValveOfClass(Class c);

    /**
     * <p>Return the first valve of the pipeline that is an instance
     * of a given class or interface.</p>
     * @param c the class or interface
     * @return a Valve instance, or null if no valve matches.
     */
    public Valve getFirstValveOfClass(Class c);
    // END [added by Pascal Aubry for CAS authentication]

}
